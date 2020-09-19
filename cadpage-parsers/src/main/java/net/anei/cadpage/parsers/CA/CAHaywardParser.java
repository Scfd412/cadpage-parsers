package net.anei.cadpage.parsers.CA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;


public class CAHaywardParser extends FieldProgramParser {

  public CAHaywardParser() {
    super("HAYWARD", "CA", 
          "ADDRCITY X X/Z? GPS1 GPS2 PLACE CODE CALL DATETIME INFO/N+? UNIT ID SKIP END");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@hayward-ca.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Incident Notification")) return false;
    if (body.endsWith(" /")) body += ' ';
    return parseFields(body.split(" / ", -1), data);
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}");
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("GPS1")) return new GPSField(1, GPS_PTN, true);
    if (name.equals("GPS2")) return new GPSField(2, GPS_PTN, true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d{1,2}/\\d{1,2}/\\d{4} +\\d{1,2}:\\d{2}:\\d{2}", true);
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
}
