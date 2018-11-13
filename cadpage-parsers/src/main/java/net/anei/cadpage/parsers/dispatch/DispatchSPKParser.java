package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchSPKParser extends HtmlProgramParser {
  
  private static final Map<String, Field> FIELD_MAP = new HashMap<String, Field>(); 

  public DispatchSPKParser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  public DispatchSPKParser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
         "CURDATETIME? Incident_Information%EMPTY! CAD_Incident:ID? ( Event_Code:CALL! THRD_PRTY_INFO+? | Event_Code_Description:CALL! | ) Priority:PRI? Incident_Disposition:SKIP? DATA<+? INFO/N<+?", 
         "table|tr");
    FIELD_MAP.put("Location", getField("ADDRCITY"));
    FIELD_MAP.put("Intersection", getField("SKIP"));
    FIELD_MAP.put("Zip Code", getField("ZIP"));
    FIELD_MAP.put("Community", getField("CITY"));
    FIELD_MAP.put("Location Information", getField("PLACE"));
    FIELD_MAP.put("Location and POI Information", getField("PLACE"));
    FIELD_MAP.put("Incident Number", getField("ID"));
    FIELD_MAP.put("L/L", getField("GPS"));
    FIELD_MAP.put("Cross Street", getField("X"));
    FIELD_MAP.put("Apartment", getField("APT"));
    FIELD_MAP.put("Apt", getField("APT"));
    FIELD_MAP.put("Building", getField("BLDG"));
    FIELD_MAP.put("Bldg", getField("BLDG"));
    FIELD_MAP.put("Caller information", getField("EMPTY"));
    FIELD_MAP.put("Caller Source", getField("SKIP"));
    FIELD_MAP.put("Caller Phone", getField("PHONE"));
    FIELD_MAP.put("Caller Name",  getField("NAME"));
    FIELD_MAP.put("Caller Location", getField("CALLER_LOC"));
    FIELD_MAP.put("Created By", getField("SKIP"));
    FIELD_MAP.put("Responding Units", getField("UNIT"));
    FIELD_MAP.put("Areas", getField("MAP"));
  }

  private String callerLocField;
  
  private boolean dispatchTime;
  private String times;
  private Set<String> unitSet = new HashSet<String>();
  
  private enum InfoType { CAD_TIMES, REMARKS, UNIT_INFO, UNIT_INFO2, UNIT_STATUS };
  private InfoType infoType;
  private int colNdx;
  
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    
    if (!subject.contains(" reaches status ") && 
        !subject.contains(" gets ") &&
        !subject.contains("has a Service Request status change")) return false;
    
    callerLocField = null;
    dispatchTime = false;
    times = null;
    unitSet.clear();
    infoType = null;
    colNdx = -1;
    
    if (!super.parseHtmlMsg(subject, body, data)) return false;

    if (data.strCall.length() == 0) data.strCall = "ALERT";
    
    if (data.strAddress.length() == 0 && callerLocField != null) parseAddress(callerLocField, data);
    if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(times, "\n", data.strSupp);
    unitSet.clear();
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " CALL";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CURDATETIME")) return new BaseDateTimeField();
    if (name.equals("ID")) return new IdField("\\d[-0-9]{8,}(?:\\.\\d{3})?|", true);
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("THRD_PRTY_INFO")) return new BaseThirdPartyInfoField();
    if (name.equals("DATA")) return new BaseDataField();

    if (name.equals("ADDRCITY")) return new BaseAddressCityField();
    if (name.equals("CALLER_LOC")) return new BaseCallerLocField();
    if (name.equals("CITY")) return new BaseCityField();
    if (name.equals("ZIP")) return new BaseZipField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("BLDG")) return new BaseBuildingField();
    if (name.equals("APT")) return new BaseAptField();
    if (name.equals("UNIT")) return new BaseUnitField();
    
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("As of (\\d\\d?/\\d\\d?/\\d\\d) (\\d\\d:\\d\\d:\\d\\d(?: [AP]M)?)");
  private class BaseDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = convertTime(match.group(2));
    }
  }
  
  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt >= 0) {
        data.strCode = field.substring(0,pt).trim();
        field = field.substring(pt+3).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private class BaseThirdPartyInfoField extends Field {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      
      if (field.startsWith("3rd Party Complaint: Medical Complaint: ")) {
        data.strCall = field.substring(40).trim();
        return true;
      }
      
      if (field.startsWith("3rd Party Code: Dispatch Code: ")) {
        data.strCode = field.substring(31).trim();
        return true;
      }
      
      return false;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }
  
  /**
   * This class handles a large collection of data field that can come in any order.  
   */
  private class BaseDataField extends Field {
    
    Field procFld;
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      String key = stripFieldEnd(field, ":");
      if (INFO_KEYWORDS.containsKey(key)) return false;
      if (field.startsWith("POI Information:")) return false;
      if (field.startsWith("This message")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      
      if (field.startsWith("<|") && field.endsWith("|>")) {
        if (field.endsWith("table|>")) procFld = null;
        return;
      }
      
      // See if field contains a recognized keyword
      Field tmpFld;
      int pt = field.indexOf(':');
      if (pt >= 0) {
        tmpFld = FIELD_MAP.get(field.substring(0,pt));
        if (tmpFld != null) field = field.substring(pt+1).trim();
      } else {
        tmpFld = FIELD_MAP.get(field);
        if (tmpFld != null) field = "";
      }
      
      if (tmpFld != null) procFld = tmpFld;
      
      // If we have a field processor, invoke it to process this field
      if (procFld != null) {
        procFld.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      // should never be called
      throw new RuntimeException("BaseDataField.getFieldNames() should never be called");
    }
    
    @Override
    public Field getProcField() {
      return procFld;
    }
  }
  
  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      
      // There can be multiple Location: keywords, but the second
      // always seems to be a cell tower location that we can ignore
      // Unless the first address is UNKNOWN in which case, accept the second one
      if (data.strAddress.length() == 0 || data.strAddress.equals("UNKNOWN")) {
        data.strAddress = "";
        super.parse(field, data);
      }
    }
  }
  
  private class BaseCallerLocField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      callerLocField = field;
    }
  }
  
  private class BaseCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      super.parse(field, data);
    }
  }
  
  private class BaseZipField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      data.strCity = field;
    }
  }
  
  private class BaseCrossField extends CrossField {
    
    @Override
    public void parse(String field, Data data) {
      
      // Cross streets tend to be duplicated a lot :(
      if (field.length() == 0) return;
      if (data.strAddress.contains("&")) return;
      if (data.strAddress.contains(field)) return;
      if (data.strCross.contains(field)) return;
      super.parse(field, data);
    }
  }
  
  private class BaseBuildingField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      data.strApt = append(data.strApt, " ", "Bldg:" + field);
    }
  }
  
  private class BaseAptField extends AptField {
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      String apt = data.strApt;
      String bldg = "";
      int pt = apt.indexOf("Bldg:");
      if (pt >= 0) {
        bldg = apt.substring(pt);
        apt = apt.substring(0,pt).trim();
      }
      apt = append(apt, "-", field);
      apt = append(apt, " ", bldg);
      data.strApt = apt;
    }
  }
  
  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      for (String unit : field.split(",")) {
        addUnit(unit.trim(), data);
      }
    }
  }
  
  private static final Pattern TIMES_PTN = Pattern.compile("Call (.*?) Time: +(\\d\\d?/\\d\\d?/\\d\\d) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final Pattern INFO_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d\\d) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private class BaseInfoField extends InfoField {
    
    boolean unitTag = false;
    String unit = null;
    List<String> statusList = null;
    boolean statusLock = false;
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("This message")) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      
      // Special Html tag processing
      if (field.startsWith("<|") && field.endsWith("|>")) {
        if (field.equals("<|/table|>") && infoType != InfoType.UNIT_INFO2) {
          infoType = InfoType.REMARKS;
          colNdx = -1;
        }
        else if (field.equals("<|tr|>")) {
          colNdx = 0;
          if (statusList != null) statusLock = true;
        }
        return;
      }
      if (colNdx >= 0) colNdx++;
      
      String key = stripFieldEnd(field, ":");
      InfoType tmp = INFO_KEYWORDS.get(key);
      if (tmp != null || INFO_KEYWORDS.containsKey(key) ||
          field.startsWith("POI Information:")) {
        infoType = tmp;
        if (infoType == InfoType.UNIT_INFO && !field.endsWith(":")) infoType = InfoType.UNIT_INFO2;
        if (infoType != null) {
          switch (infoType) {
          case CAD_TIMES:
            if (times == null) times = field;
            else times = times + '\n' + field;
            break;
            
          case UNIT_INFO:
            colNdx = -1;
            break;
            
          case UNIT_STATUS:
            if (times == null) times = field;
            else times = times + '\n' + field;
            colNdx = -1;
            break;
            
          case UNIT_INFO2:
            if (times == null) times = field;
            else times = times + '\n' + field;
            colNdx = -1;
            unitTag = false;
            unit = null;
            statusList = null;
            break;
            
          default:
          }
        }
        return;
      }
      
      if (infoType == null) return;
      
      switch (infoType) {
      case CAD_TIMES:
        Matcher match = TIMES_PTN.matcher(field);
        if (match.matches()) {
          String type = match.group(1);
          if (!dispatchTime && type.equalsIgnoreCase("Dispatched")) {
            dispatchTime = true;
            data.strDate = match.group(2);
            data.strTime = convertTime(match.group(3));
          } else if (type.equals("On Scene") ||
                     type.equals("Closed") ||
                     type.equalsIgnoreCase("Available")) data.msgType = MsgType.RUN_REPORT;  
        }
        times = times + '\n' + field;
        return;
        
      case REMARKS:
        if (INFO_TIME_PTN.matcher(field).matches()) return;
        if (field.startsWith("Responding Units:")) {
          for (String unit : field.substring(17).split(",")) {
            addUnit(unit.trim(), data);
          }
        } else if (field.startsWith("Caller Name:")) {
          data.strName = cleanWirelessCarrier(field.substring(12).trim());
        } else if (field.startsWith("Problem:")) {
          field = field.substring(8).trim();
          if (data.strCall.equals("PRO QA IN PROGESS")) data.strCall = field;
          else data.strCall = append(data.strCall, " - ", field);
        } else if (field.startsWith("Callback:")) {
          data.strPhone = field.substring(9).trim();
        } else if (!field.equals("Number of patients: 1")) {
          super.parse(field, data);
        }
        return;
        
      case UNIT_INFO:
        if (colNdx != 1) return;
        if (!field.equals("Unit")) addUnit(field, data);
        return;
        
      case UNIT_STATUS:
        if (field.equals("Date/Time")) return;
        if (field.equals("Unit")) return;
        if (field.equals("Status")) return;
        if (field.equals("Unit Location/Remarks")) return;
        String delim = colNdx == 1 ? "\n" : "   ";
        times = times + delim + field;
        
        if (colNdx == 2) addUnit(field, data);
        
        if (!dispatchTime && field.equals("Dispatched")) {
          match = INFO_TIME_PTN.matcher(getRelativeField(-2));
          if (match.matches()) {
            dispatchTime = true;
            data.strDate = match.group(1);
            data.strTime = convertTime(match.group(2));
          }
        }
        
        if (field.equalsIgnoreCase("On Scene") || 
            field.equalsIgnoreCase("Available")) data.msgType = MsgType.RUN_REPORT;
        return;
      
      case UNIT_INFO2:
        if (colNdx == 1) {
          if (field.equals("Unit")) {
            unitTag = true;
            statusList = null;
            return;
          }
          
          if (unit != null && UNIT_STATUS_SET.contains(field)) {
            statusList = new ArrayList<>();
            statusList.add(field);
            statusLock = false;
            return;
          }
        }
        
        if (unitTag) {
          unitTag = false;
          unit = field;
          addUnit(field, data);
          return;
        }
        
        if (statusList != null) {
          if (!statusLock) {
            statusList.add(field);
          } else if (INFO_TIME_PTN.matcher(field).matches()) {
            if (colNdx <= statusList.size()) {
              String status = statusList.get(colNdx-1);
              String line = field + "   " + unit + "   " + status;
              times = append(times, "\n", line);
              if (status.equals("ON SCENE") || status.equals("AVAILABLE")) data.msgType = MsgType.RUN_REPORT;
            }
          }
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "DATE TIME INFO UNIT CALL NAME PHONE";
    }
  }
  
  private void addUnit(String unit, Data data) {
    unit = unit.replace(' ', '_');
    if (unitSet.add(unit)) data.strUnit = append(data.strUnit, ",", unit);
  }
  
  private static String convertTime(String time) {
    if (time.endsWith("M")) {
      if (Integer.parseInt(time.substring(0, 2)) > 12) return time.substring(0, 8);
      try {
        time = TIME_FMT2.format(TIME_FMT1.parse(time));
      } catch (ParseException ex) {
        throw new  RuntimeException(ex);
      }
    }
    return time;
  }
  private static final DateFormat TIME_FMT1 = new SimpleDateFormat("hh:mm:ss aa");
  private static final DateFormat TIME_FMT2 = new SimpleDateFormat("HH:mm:ss");
  
  private static final Set<String> UNIT_STATUS_SET = new HashSet<String>(Arrays.asList(
      "DISPATCHED", "EN ROUTE", "ON SCENE", "AVAILABLE"
  ));
  
  private static final Map<String, InfoType> INFO_KEYWORDS = new HashMap<String,InfoType>();
  static {
    INFO_KEYWORDS.put("CAD Times", InfoType.CAD_TIMES);
    
    INFO_KEYWORDS.put("Remarks/Narratives", InfoType.REMARKS);
    INFO_KEYWORDS.put("Notices", InfoType.REMARKS);
    INFO_KEYWORDS.put("Notes", InfoType.REMARKS);
    INFO_KEYWORDS.put("EMS DISPATCH PROTOCOL", InfoType.REMARKS);
    INFO_KEYWORDS.put("Dispatch", InfoType.REMARKS);

    INFO_KEYWORDS.put("Unit Information", InfoType.UNIT_INFO);
    
    INFO_KEYWORDS.put("Unit Status", InfoType.UNIT_STATUS);
    
    INFO_KEYWORDS.put("POI Information", null);
    INFO_KEYWORDS.put("Priors", null);
    INFO_KEYWORDS.put("Case Numbers", null);
    INFO_KEYWORDS.put("Incident Log", null);
  }
  
  @Override
  protected boolean parseFields(String[] fields, Data data) {
    fixKeywords(fields);
    return super.parseFields(fields, data);
  }

  private static void fixKeywords(String[] fields) {
    for (int ndx = 0; ndx < fields.length; ndx++) {
      String field = fields[ndx];
      if (field.startsWith("Autosend.") || field.startsWith("autosend.") || field.startsWith("call")) {
        CodeTable.Result res = FIX_KEYWORD_TABLE.getResult(field);
        if (res != null) {
          field = res.getDescription() + field.substring(res.getCode().length());
          fields[ndx] = field;
        }
      }
    }
  }
  
  private static final CodeTable FIX_KEYWORD_TABLE = new CodeTable(
    "autosend.incident.header",                 "As of",
    "autosend.incident.incidentInformation",    "Incident Information",
    "autosend.incident.tracking.number",        "CAD Incident ",
    "Autosend.Options.eventCode",               "Event Code",
    "autosend.incident.location",               "Location",
    "autosend.incident.community",              "Community",
    "Autosend.Options.locationInformation",     "Location Information",
    "Autosend.Options.crossStreet",             "Cross Street",
    "Autosend.Options.apartment",               "Apartment",
    "Autosend.Options.building",                "Building",
    "autosend.incident.callerInformation",      "Caller information",
    "Autosend.Options.callerPhone",             "Caller Phone",
    "Autosend.Options.callerName",              "Caller Name",
    "Autosend.Options.callerLocation",          "Caller Location",
    "autosend.incident.location",               "Location",
    "autosend.incident.community",              "Community",
    "Autosend.Options.incidentTimes",           "CAD Times",
    "callCreatedTime",                          "Call Created Time",
    "callStatusTime",                           "Call Dispatched Time",
    "Autosend.Options.time",                    "Incident Creation Time",
    "Autosend.Options.unitStatus",              "Unit Status",
    "autosend.incident.logs.dateTime",          "Date/Time",
    "autosend.unit.history.unit",               "Unit",
    "autosend.unit.history.unitStatus",         "Status",
    "autosend.unit.history.unitLocation",       "Unit Location/Remarks",
    "Autosend.Options.unitInformation",         "Unit Information",
    "autosend.unit.history.unit",               "Unit",
    "autosend.unit.information.unitOrg",        "Org",
    "autosend.unit.information.name",           "Name",
    "autosend.unit.information.area",           "Area",
    "autosend.unit.information.types",          "Types",
    "Autosend.Options.caseNumbers",             "Case Numbers",
    "Autosend.Options.log",                     "Incident Log",
    "Autosend.Options.respondingUnits",         "Responding Units",
    "Autosend.Options.narratives",              "Remarks/Narratives",
    "Autosend.Options.notices",                 "Notices"
  );
}
