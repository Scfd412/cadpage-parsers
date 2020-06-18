package net.anei.cadpage.parsers.SC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class SCLexingtonCountyParser extends DispatchOSSIParser {
  
  public SCLexingtonCountyParser() {
    super(CITY_CODES, "LEXINGTON COUNTY", "SC",
          "( CANCEL ADDR CITY | FYI? SRC? CALL ADDR! ( X/Z PLACE CITY | X_PLACE CITY | CITY | ) UNIT? ) INFO+");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "CAD@lex-co.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CAD")) return false;
    body = "CAD:" + body;
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("(?!MVC)[A-Z]{3,4}", true);
    if (name.equals("X_PLACE")) return new MyCrossPlaceField();
    if (name.equals("UNIT")) return new UnitField("(?:IBAT|[A-Z]+\\d+)(?:,[A-Z0-9]+)*", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyCrossPlaceField extends Field {

    @Override
    public void parse(String field, Data data) {
      if (isValidAddress(field)) {
        data.strCross = field;
      } else {
        data.strPlace = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "X PLACE";
    }
  }
  
  private Pattern INFO_TRIM_PTN = Pattern.compile("[-* ]*(.*?)[-* ]*");
  private Pattern INFO_CH_PTN = Pattern.compile("OPS *\\d+", Pattern.CASE_INSENSITIVE);
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_TRIM_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      
      if (INFO_CH_PTN.matcher(field).matches()) {
        data.strChannel = field;
      } else {
        data.strSupp = append(data.strSupp, "\n", field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CH INFO";
    }
  }
  
  private static final Pattern I_20_26_PTN = Pattern.compile("(\\d{2,3})(\\d\\d) (20|26) [EW]");
  
  @Override
  protected String adjustGpsLookupAddress(String address) {
    Matcher match = I_20_26_PTN.matcher(address);
    if (match.matches()) {
      String mm = match.group(1);
      int dec = Integer.parseInt(match.group(2));
      if (dec >= 50) mm = Integer.toString(Integer.parseInt(mm)+1);
      address = mm + "00 " + match.group(3);
    }
    return address;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "3700 20",                              "+33.796521,-81.471337",
      "3800 20",                              "+33.807833,-81.460559",
      "3900 20",                              "+33.818890,-81.449192",
      "4000 20",                              "+33.828163,-81.435913",
      "4100 20",                              "+33.837301,-81.422582",
      "4200 20",                              "+33.847296,-81.409940",
      "4300 20",                              "+33.857192,-81.397318",
      "4400 20",                              "+33.868653,-81.386676",
      "4500 20",                              "+33.880337,-81.376558",
      "4600 20",                              "+33.888953,-81.362586",
      "4700 20",                              "+33.897536,-81.348646",
      "4800 20",                              "+33.906110,-81.334744",
      "4900 20",                              "+33.914433,-81.320490",
      "5000 20",                              "+33.921162,-81.305055",
      "5100 20",                              "+33.927082,-81.289327",
      "5200 20",                              "+33.935197,-81.274980",
      "5300 20",                              "+33.943137,-81.260362",
      "5400 20",                              "+33.951070,-81.245686",
      "5500 20",                              "+33.958544,-81.230907",
      "5600 20",                              "+33.963778,-81.214629",
      "5700 20",                              "+33.969878,-81.198919",
      "5800 20",                              "+33.983341,-81.192632",
      "5900 20",                              "+33.993030,-81.180969",
      "6000 20",                              "+34.003278,-81.168789",
      "6100 20",                              "+34.009591,-81.153434",
      "6200 20",                              "+34.015936,-81.139654",
      "6300 20",                              "+34.027377,-81.126181",
      "3700 26",                              "+33.796521,-81.471337",
      "3800 26",                              "+33.807833,-81.460559",
      "3900 26",                              "+33.818890,-81.449192",
      "4000 26",                              "+33.828163,-81.435913",
      "4100 26",                              "+33.837301,-81.422582",
      "4200 26",                              "+33.847296,-81.409940",
      "4300 26",                              "+33.857192,-81.397318",
      "4400 26",                              "+33.868653,-81.386676",
      "4500 26",                              "+33.880337,-81.376558",
      "4600 26",                              "+33.888953,-81.362586",
      "4700 26",                              "+33.897536,-81.348646",
      "4800 26",                              "+33.906110,-81.334744",
      "4900 26",                              "+33.914433,-81.320490",
      "5000 26",                              "+33.921162,-81.305055",
      "5100 26",                              "+33.927082,-81.289327",
      "5200 26",                              "+33.935197,-81.274980",
      "5300 26",                              "+33.943137,-81.260362",
      "5400 26",                              "+33.951070,-81.245686",
      "5500 26",                              "+33.958544,-81.230907",
      "5600 26",                              "+33.963778,-81.214629",
      "5700 26",                              "+33.969878,-81.198919",
      "5800 26",                              "+33.983341,-81.192632",
      "5900 26",                              "+33.993030,-81.180969",
      "6000 26",                              "+34.003278,-81.168789",
      "6100 26",                              "+34.009591,-81.153434",
      "6200 26",                              "+34.015936,-81.139654",
      "6300 26",                              "+34.027377,-81.126181",
      "8900 26",                              "+34.195282,-81.354703",
      "9000 26",                              "+34.187387,-81.339795",
      "9100 26",                              "+34.178810,-81.325572",
      "9200 26",                              "+34.173023,-81.309836",
      "9300 26",                              "+34.167619,-81.294131",
      "10900 26",                             "+34.013984,-81.108085",
      "11000 26",                             "+33.999722,-81.109000",
      "11100 26",                             "+33.985673,-81.106932",
      "11200 26",                             "+33.972853,-81.102856",
      "11300 26",                             "+33.956236,-81.093152",
      "11400 26",                             "+33.945402,-81.089566",
      "11500 26",                             "+33.934362,-81.079126",
      "11600 26",                             "+33.923583,-81.068039",
      "11700 26",                             "+33.912644,-81.057452",
      "11800 26",                             "+33.900419,-81.047774",
      "11900 26",                             "+33.888044,-81.039042",
      "12000 26",                             "+33.873681,-81.035910",
      "12800 26",                             "+33.770990,-80.975264",
      "12900 26",                             "+33.763811,-80.960550",
      "13000 26",                             "+33.755147,-80.946714",
      "13100 26",                             "+33.749788,-80.943194"
 
  });
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BA", "BATESBURG",
      "BL", "BATESBURG LEESVILE",
      "CA", "CAYCE",
      "CH", "CHAPIN",
      "CO", "COLUMBIA",
      "GA", "GASTON",
      "GI", "GILBERT",
      "IR", "IRMO",
      "LE", "LEESVILLE",
      "LX", "LEXINGTON",
      "PE", "PELION",
      "PI", "PINE RIDGE",
      "SC", "SOUTH CONGAREE",
      "SP", "SPRINGDALE",
      "SW", "SWANSEA",
      "WC", "WEST COLUMBIA"
  }); 
}
