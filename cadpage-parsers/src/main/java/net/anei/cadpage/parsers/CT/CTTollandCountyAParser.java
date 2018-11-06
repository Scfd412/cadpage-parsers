package net.anei.cadpage.parsers.CT;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class CTTollandCountyAParser extends SmartAddressParser {
  
  public CTTollandCountyAParser() {
    super(CTTollandCountyParser.CITY_LIST, "TOLLAND COUNTY", "CT");
    removeWords("COURT", "KNOLL", "ROAD", "STREET", "TERRACE");
    addRoadSuffixTerms("CMNS", "COMMONS");
    setupSaintNames("PHILIPS");
    setupMultiWordStreets(
        "WHITNEY T FERGUSON III"
    );
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "@TollandCounty911.org,@TollandCounty911.com,messaging@iamresponding.com";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("[A-Z]+");
  private static final Pattern BAD_PTN = Pattern.compile("\\d{10} .*", Pattern.DOTALL);
  
  private static final Pattern MASTER1 = Pattern.compile("([^,]*?)(?:, ([A-Za-z ]+))? / (.*?) Cross Street (?:(.*?) )?(?:(Station \\d+) )?(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M) (\\d{4}-\\d{8}\\b.*)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  
  private static final Pattern MASTER2 = Pattern.compile("(.*?) (\\d\\d:\\d\\d)(?: (.*?))?(?: (\\d{4}-\\d{8}))?");
  private static final Pattern FLR_PTN = Pattern.compile("(\\d+)(?:ST|ND|RD|TH) *(?:FLOOR|FLR?)");
  private static final Pattern APT_PTN = Pattern.compile("(?:UNIT|TRLR|TRAILER|APT|LOT|FLR?)[- ]*(.*)|[A-Z] *\\d*|\\d+[A-Z]?|\\d+FL");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    do {
      
      if (subject.equals("TN Alert")) break;
      
      if (SUBJECT_PTN.matcher(subject).matches()) {
        data.strSource = subject;
        break;
      }
      
      if (body.startsWith("TN Alert / ")) {
        body = body.substring(11);
        break;
      }
      
      // But if we don't have anything, accept that too
    } while (false);
    
    // Rule out variant of CTTollandCountyB
    if (BAD_PTN.matcher(body).matches()) return false;
    
    body = body.replace('\n', ' ');
    
    // Check for variant 1 format
    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("ADDR APT CITY CALL X UNIT DATE TIME ID");
      parseAddress(match.group(1).trim(), data);
      data.strCity = getOptGroup(match.group(2));
      data.strCall = match.group(3).trim();
      String cross = getOptGroup(match.group(4));
      if (!cross.equals("No Cross Streets Found")) data.strCross = cross;
      data.strUnit = getOptGroup(match.group(5));
      data.strDate = match.group(6);
      setTime(TIME_FMT, match.group(7), data);
      data.strCallId = match.group(8);
      return true;
    }
    
    match = MASTER2.matcher(body);
    if (match.matches()) {
      setFieldList("SRC ADDR APT CITY PLACE CALL TIME X ID");
      body = match.group(1).trim();
      data.strTime = match.group(2);
      String cross = getOptGroup(match.group(3));
      cross = stripFieldStart(cross, "Cross Street");
      if (!cross.equals("No Cross Streets Found")) data.strCross = cross;
      data.strCallId = getOptGroup(match.group(4));
      
      // We are invoking the smart address parser strictly to find city, it
      // shouldn't have to do much parsing.  If it doesn't find a city, bail out.
      // The slash confuses the parse logic, so switch it to something unusual
      // Ditto for parrens
      body = FLR_PTN.matcher(body).replaceAll("FLR $1");
      body = escape(body);
      parseAddress(StartType.START_ADDR, FLAG_EMPTY_ADDR_OK, body, data);
      String sAddr = unescape(data.strAddress);
      data.strApt = unescape(data.strApt);
      body = unescape(getLeft());
      
      // Address always has a slash, which the address parser turned to an ampersand
      // What is in front of that becomes the address
      int pt = sAddr.indexOf('/');
      if (pt >= 0) {
        
        // Use smart address parser to extract trailing apt
        parseAddress(StartType.START_ADDR, FLAG_NO_CITY, sAddr.substring(0,pt).trim(), data);
        data.strApt = append(data.strApt, " - ", getLeft());
        
        sAddr = sAddr.substring(pt+1).trim();
        sAddr = stripFieldEnd(sAddr, "/");
        
        // if what comes after the slash is a street name
        // If not, put it in the apt field
        match = APT_PTN.matcher(sAddr);
        if (match.matches()) {
          String apt = match.group(1);
          if (apt == null) apt = sAddr;
          if (!data.strApt.equals(apt)) data.strApt = append(apt, "-", data.strApt);
        }
        else if (isValidAddress(sAddr)) {
          data.strAddress = append(data.strAddress, " & ", sAddr);
        } else {
          data.strPlace = append(data.strPlace, " - ", sAddr);
        }
      }
      
      // Once in a blue moon, the slash ends up in the apartment field
      else if (data.strApt.endsWith("/")) {
        data.strApt = data.strApt.substring(0,data.strApt.length()-1).trim();
      } else {
        pt = data.strApt.indexOf('/');
        if (pt >= 0) {
          data.strApt = append(data.strApt.substring(0,pt).trim(), " - ", data.strApt.substring(pt+1).trim());
        }
        else body = stripFieldStart(body, "/");
      }
      
      // Everything from city to time field is the call description
      body = stripFieldStart(body, "*");
      
      // See if we can split the remaining body into place name, call, and info
      for (int j = 0; j<body.length()-2; j++) {
        if (Character.isLetter(body.charAt(j))) {
          String call = CALL_LIST.getCode(body.substring(j), true);
          if (call != null) {
            String place = body.substring(0,j).trim();
            data.strPlace = append(data.strPlace, " - ", place);
            data.strCall = body.substring(j);
            return true;
          }
        }
      }
      
      // No go, just assign everything as the call description
      data.strCall = body;
      return true;
    }
    
    return false;
  }
  
  private static final String[] ESCAPE_CODES = new String[]{
    "/", "\\s",
    "(", "\\lp",
    ")", "\\rp",
    "[", "\\lb",
    "]", "\\rb"
  };
  
  private static String escape(String fld) {
    for (int j = 0; j<ESCAPE_CODES.length; j+=2) {
      fld = fld.replace(ESCAPE_CODES[j], ESCAPE_CODES[j+1]);
    }
    return fld;
  }
  
  private static String unescape(String fld) {
    for (int j = 0; j<ESCAPE_CODES.length; j+=2) {
      fld = fld.replace(ESCAPE_CODES[j+1], ESCAPE_CODES[j]);
    }
    return fld;
  }
  
  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "EXIT 64",  "41.823335, -72.499277",
      "EXIT 66",  "41.833940, -72.463705",
      "EXIT 67",  "41.854601, -72.429307",
      "EXIT 65",  "41.826197, -72.487915"

  });

  private static final CodeSet CALL_LIST = new CodeSet(
      "<New Call>",
      "Active Violence/Shooter",
      "Aircraft Accident",
      "ALS",
      "Appliance Fire",
      "BLS",
      "Bomb Threat",
      "Brush Fire",
      "CARDIAC ARREST",
      "Chimney Fire",
      "CO No Symptoms",
      "CO With Symptoms",
      "Cover Assignment",
      "Dumpster/Debris Fire",
      "Electrical Fire",
      "Fire Alarm",
      "Fuel Spill",
      "Hazardous Materials",
      "Lift Assist",
      "Machinery Entrapment",
      "Mutual Aid Fire",
      "Natural Gas/Propane Leak",
      "Officer Call",
      "OFFICER CALL TN.",
      "Outside Fire",
      "Search and Rescue",
      "Service Call",
      "Smoke Detector Activation",
      "Smoke In Building",
      "Smoke/Odor Investigation",
      "Standby",
      "Structure Fire",
      "THIS IS ONLY A TEST",
      "Tree/Wires Down",
      "Unknown Type Fire",
      "Vehicle Accident W/O Injuries",
      "Vehicle Accident",
      "Vehicle Accident/HeadOn",
      "Vehicle Fire",
      "Wires Burning/Arcing"
  );
}
