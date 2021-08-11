package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchA82Parser extends FieldProgramParser {

  public DispatchA82Parser(String defCity, String defState) {
    super(defCity, defState,
          "ID ( ADDRCITYST PLACE X MASH1+? EMPTY! UNITS:UNIT! St_Rmk:MAP/C? Grid_Map:MAP/L? EMPTY? INFO/N+? URL END " +
             "| CALL! CALL/SDS+? ADDRCITYST X MASH2! UNITS:UNIT! ST_RMK:MAP/C? INFO/N+ " +
             ") END");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CFS Page") && !subject.equals("Message from Dispatch")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("(\\d{8})(?: MANUAL PAGE)?", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("MASH1"))  return new MyMash1Field();
    if (name.equals("MASH2")) return new MyMash2Field();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("URL")) return new InfoUrlField("https?:.*");
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern MASH1_PTN = Pattern.compile("\\[([A-Z]+) \\(([^()]+)\\) (?:\\(Pri:(\\d+)\\) )?(?:\\(Esc:(\\d+)\\) )?- DIST: (\\S+) - GRID: (\\S+)\\]");
  private class MyMash1Field extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("[") || !field.endsWith("]")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      Matcher match = MASH1_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strSource = merge(data.strSource, match.group(1));
      if (data.strCall.length() == 0) data.strCall = match.group(2).trim();
      if (data.strPriority.length() == 0) data.strPriority = append(getOptGroup(match.group(3)), "/", getOptGroup(match.group(4)));
      data.strBox = merge(data.strBox, match.group(5));
      data.strMap = merge(data.strMap, match.group(6));
    }

    @Override
    public String getFieldNames() {
      return "SRC CALL PRI BOX MAP";
    }
  }

  private static final Pattern MASH2_SEP_PTN = Pattern.compile("\\] *\\[");
  private static final Pattern MASH2_PTN = Pattern.compile("(\\S+) DIST: (\\S*) GRID: (\\S*)\\b *");
  private class MyMash2Field extends Field {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("[") || !field.endsWith("]")) abort();
      field = field.substring(1, field.length()-1).trim();
      field = MASH2_SEP_PTN.matcher(field).replaceAll(" ");
      while (!field.isEmpty()) {
        Matcher match = MASH2_PTN.matcher(field);
        if (!match.lookingAt()) abort();
        data.strSource = merge(data.strSource, match.group(1));
        data.strBox = merge(data.strBox, match.group(2));
        data.strMap = merge(data.strMap, match.group(3));
        field = field.substring(match.end());
      }
    }

    @Override
    public String getFieldNames() {
      return "SRC BOX MAP";
    }
  }

  private static String merge(String base, String field) {
    if (base.contains(field)) return base;
    return append(base, ",", field);
  }

  private Pattern INFO_PFX_PTN = Pattern.compile("Rmk\\d+: \\[\\d\\d?:\\d\\d:\\d\\d\\]: *|CFS RMK \\d\\d:\\d\\d *");
  private Pattern INFO_JUNK_PTN = Pattern.compile("\\{\\S+ +\\d\\d:\\d\\d\\}|<NO CALL REMARKS>|\\[SENT: \\d\\d:\\d\\d:\\d\\d\\]");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_PFX_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapAddress(String sAddress, boolean cross) {
    return SMALL_BLOCK_PTN1.matcher(sAddress).replaceAll("$1_$2");
  }
  private static final Pattern SMALL_BLOCK_PTN1 = Pattern.compile("(SMALL) +(BLOCK)", Pattern.CASE_INSENSITIVE);

  @Override
  public String postAdjustMapAddress(String sAddress) {
    return SMALL_BLOCK_PTN2.matcher(sAddress).replaceAll("$1 $2");
  }
  private static final Pattern SMALL_BLOCK_PTN2 = Pattern.compile("(SMALL)_(BLOCK)", Pattern.CASE_INSENSITIVE);
}
