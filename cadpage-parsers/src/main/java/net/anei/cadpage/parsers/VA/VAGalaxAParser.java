package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchDAPROParser;

/**
 * Grayson County, VA
 */
public class VAGalaxAParser extends DispatchDAPROParser {
  
  public VAGalaxAParser() {
    super("GALAX", "VA");  
    setupCallList(CALL_SET);
    setupMultiWordStreets(
        "A LINEBERRY",
        "ADAMS BRANCH",
        "ALPINE CREST",
        "ANVIL ROCK",
        "APPLE TREE",
        "BALDWIN FELTS",
        "BAYWOOD STORE",
        "BEAR DEN",
        "BEAVER DAM",
        "BIG RIDGE",
        "BLUE DEVIL",
        "BLUE RIDGE CC",
        "BLUE RIDGE MILL",
        "BLUE RIDGE",
        "BREEZY RIDGE",
        "BRIDLE CREEK",
        "BUCK WOODS",
        "BURNING BUSH",
        "CAMP FORK CREEK",
        "CARROLL HOUSE",
        "CARTER PINES",
        "CEDAR BROOK",
        "CHANCES CREEK",
        "CHESTNUT GROVE",
        "CITY VIEW",
        "CLOVER LEAF",
        "COAL CREEK",
        "COMERS ROCK",
        "COON RIDGE",
        "COULSON CHURCH",
        "COUNTRY CLUB",
        "COUNTY LINE",
        "CREEK VIEW",
        "CROOKED OAK",
        "CROSS ROADS",
        "DAVID RIDGE",
        "DOUBLE CABIN",
        "DUCK ROOST",
        "DUSTY RIDGE\\THUNDER RIDGE",
        "ELK CREEK",
        "ELK HORN",
        "EXCELSIOR SCHOOL",
        "FANCY GAP",
        "FARMERS MARKET",
        "FISHERS GAP",
        "FOREST OAK",
        "FOX CREEK",
        "FOX RIDGE",
        "FROZEN LAKE",
        "GLADE CREEK",
        "GLEN RIDGE",
        "GOLD HILL",
        "GRANGE HALL",
        "GREASY CREEK",
        "GREEN ACRES",
        "GROUNDHOG MTN",
        "HARRISON RIDGE",
        "HAWK LANDING",
        "HIDDEN PINES",
        "HIDDEN RIDGE",
        "HIDDEN VALLEY",
        "HIGH HILL",
        "HIGH POINT",
        "HIRAM STANLEY",
        "HONEYCUTT DAM",
        "HORSE SHOE",
        "HURRICANE RIDGE",
        "INDIAN VALLEY",
        "INDUSTRIAL PARK",
        "IRON RIDGE",
        "JACKSON MILL",
        "JEB STUART",
        "JOHN'S CREEK",
        "JOY RANCH",
        "KAYLA NICOLE",
        "L J'S",
        "LAUREL MOUNTAIN",
        "LAUREL RIDGE",
        "LAUREL VALLEY",
        "LIME KILN",
        "LITTLE BIT",
        "LITTLE VINE",
        "LIVE OAK",
        "LOCUST RIDGE",
        "LONESOME DOVE",
        "LONESOME OAK",
        "MAPLE SHADE",
        "MAROON TIDE",
        "MARTHA'S KNOB",
        "MORNING VIEW",
        "MOUNTAIN VIEW",
        "MT TAYLOR",
        "MT ZION",
        "OAK GROVE",
        "ORCHARD GAP",
        "PEAR TREE",
        "PENN FORD",
        "PINE KNOLL",
        "PINE MTN",
        "PIPERS GAP",
        "POPLAR CAMP",
        "POPLAR HILL",
        "POPLAR KNOB",
        "RACKING EXPRESS",
        "RED HILL",
        "RISING SUN",
        "RIVER HILL",
        "RUSSELL'S LAKE",
        "SADDLE CREEK",
        "SNAKE CREEK",
        "SPRING VALLEY",
        "STEVENS CREEK",
        "STONE MOUNTAIN",
        "STONE RIDGE",
        "SUGAR CAMP",
        "SUMMER BREEZE",
        "TEN MILLS",
        "TERRY'S MILL",
        "THUNDER RIDGE",
        "TRADING POST",
        "TRAINING CENTER",
        "TURNER SCHOOL",
        "VASS MILL",
        "VILLAGE VIEW",
        "WARDS GAP",
        "WARDS MILL",
        "WATER PLANT",
        "WHITETOP MTN",
        "WILLIS GAP",
        "WILLOW TREE",
        "WINDING RIDGE",
        "WINDY HILL",
        "WOLFPEN RIDGE"
      );
  }
  
  @Override
  public String getFilter() {
    return "MAILBOX@GalaxVa.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('\n', ' ');
    return super.parseMsg(body, data);
  }

  private static final CodeSet CALL_SET = new CodeSet(
      "ALARM-BUSINESS",
      "ALARM-RESIDENCE",
      "ANIMAL CARCASS",
      "ASSIST EMS",
      "ASSIST FIRE",
      "ASSIST LAW ENFORCEMENT",
      "CANCEL FALSE ALARM FIRE ALARM",
      "DIRECT TRAFFIC",
      "DOMESTIC VIOLENCE",
      "E*MEDICAL DEVICE - TUBES, LINE",
      "EMS - ABDOMINAL DISTRESS",
      "EMS - ALARM",
      "EMS - ALCOHOL POISONING",
      "EMS - ALLERGIC REACTION",
      "EMS - ASTHMA RELATED",
      "EMS - ASSAULT INJURY",
      "EMS - BLOOD PRESSURE",
      "EMS - BLEEDING",
      "EMS - BREATHING DIFFICULTY",
      "EMS - CARDIAC",
      "EMS - BACK PAIN / INJURY",
      "EMS - BITE/STING",
      "EMS - BURN",
      "EMS - DIABETIC",
      "EMS - DISORIENTED",
      "EMS - DIZZINESS / VERTIGO",
      "EMS - DOA - UNKNOWN",
      "EMS - DRUG ABUSE (NOT O.D.)",
      "EMS - FAINTING",
      "EMS - FAINTING / SYNCOPE",
      "EMS - FALL",
      "EMS - FEVER",
      "EMS - FRACTURE",
      "EMS - HEAD INJURY",
      "EMS - HEAT EXPOSURE",
      "EMS - HEADACHE",
      "EMS - INTERFACILITY TRANSPORT",
      "EMS - LACERATION",
      "EMS - NAUSEA / VOMITING",
      "EMS - NO INFORMATION ON PATIEN",
      "EMS - NUMBNESS",
      "EMS - OB / GYN",
      "EMS - PAIN",
      "EMS - SEIZURE",
      "EMS - SICK/UNKNOWN/OTHER",
      "EMS - STROKE",
      "EMS - TRAUMA/INJURY",
      "EMS - RETURN TRIP",
      "EMS - UNRESPONSIVE",
      "EMS - WEAKNESS",
      "EMS - INFECTIOUS ILLNESS",
      "FIGHT",
      "FIRE - AIRCRAFT",
      "FIRE - BRUSH",
      "FIRE - FALSE ALARM CANCEL CALL",
      "FIRE - STRUCTURE",
      "FIRE - TRASH",
      "FIRE - UTILITY",
      "FIRE - VEHICLE",
      "GAS LEAK",
      "HAZARDOUS MATERIAL LEAK/SPILL",
      "LOG FOR RECORD",
      "MENTAL / EMOTIONAL/PSYC",
      "MENTAL / PSYC PATIENT TRANSPOR",
      "MOTOR VEHICLE CRASH",
      "MOTORIST ASSIST",
      "MURDER",
      "OVERDOSE",
      "POWER LINE DOWN",
      "ROAD REPAIR/ HAZARD",
      "SMOKE REPORT",
      "STABBING",
      "SUICIDE / ATTEMPT",
      "TESTING",
      "TESTING - ONLY!",
      "TOBACCO VIOLATIONS",
      "TRANSPORT CRIMINAL / NOT MENTA",
      "TRAPPED SUBJECT",
      "WANTED PERSON",
      "WELFARE CHECK"
  );
}