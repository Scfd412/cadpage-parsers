package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;


public class PATiogaCountyParser extends DispatchA48Parser {

  public PATiogaCountyParser() {
    super(CITY_LIST, "TIOGA COUNTY", "PA", FieldType.PLACE, A48_NO_CODE | A48_OPT_CALL);
    setupCallList(CALL_LIST);
    setupCities(CITY_CODES);
    setupMultiWordStreets(
        "ALDER RUN",
        "AMEIGH VALLEY",
        "ANTRIM MAIN",
        "BAILEY CREEK",
        "BAKER CREST",
        "BAPTIST HILL",
        "BIRD CREEK",
        "BUCK HILL",
        "BUCKWHEAT HOLLOW",
        "BURROWS HOLLOW",
        "BUTTON HILL",
        "CORYLAND PARK",
        "ELK RUN",
        "ENGLISH HILL",
        "GROUSE RUN",
        "GROVES HILL",
        "JACKSON CENTER",
        "JUDSON HILL",
        "LAUREL HILL",
        "MOTT TOWN",
        "PICNIC GROVE",
        "PINE HILL",
        "PONY HILL",
        "PUMPKIN HILL",
        "RILEY HILL",
        "ROARING RUN",
        "ROWLEY HILL",
        "TALL PINES",
        "TOWER HILL",
        "TROWBRIDGE STATION",
        "WHISPERING PINES",
        "WILLIAM FARRELL",
        "WOLFE RUN"
    );
  }

  @Override
  public String getFilter() {
    return "@tiogacountypa.us";
  }


  private static final Pattern TRAIL_NULL_PTN = Pattern.compile("(?:\\s+null)+$");


  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    body = TRAIL_NULL_PTN.matcher(body).replaceFirst("");

    if (!super.parseMsg(subject, body, data)) return false;

    // Description ending with 3 HRS confuses the address parser
    if (data.strAddress.startsWith("3 HRS ")) {
      data.strCall += " 3 HRS";
      data.strAddress = data.strAddress.substring(6).trim();
    }

    data.strCity = stripFieldEnd(data.strCity, " BORO");
    return true;
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.endsWith(" TWP")) city = city.substring(0, city.length()-2) + "OWNSHIP";
    return city;
  }


  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN",
      "ALLERGIES STINGS BITES DIFF SPEAKING BETWEEN BREATHS",
      "ALS MEDICAL",
      "ASSIST OTHER AGENCY",
      "AUTOMATIC FIRE ALARM",
      "BACK PAIN - NON-RECENT",
      "BACK PAIN - NON-TRAUMATIC",
      "BARN FIRE",
      "BLS MEDICAL",
      "BLS TRANSFER",
      "BREATHING PROBLEMS - ABNORMAL BREATHING",
      "BREATHING PROBLEMS - ASTHMA - DIFF SPKING BETWEEN BREATHS",
      "BREATHING PROBLEMS - CHANGING COLOR",
      "BREATHING PROBLEMS - DIFF SPEAKING BETWEEN BREATHS",
      "BREATHING PROBLEMS-OTHER LUNG PROBLEMS-ABNORMAL BREATHING",
      "CARDIAC OR RESP ARREST - OBVIOUS DEATH-UNQUESTIONABLE - COLD AND STIFF IN WARM ENVIRONMENT",
      "CARDIAC OR RESP ARREST - SUSPECTED WORKABLE ARREST - NOT BREATHING",
      "CHEST PAIN - ABNORMAL BREATHING",
      "CHEST PAIN - BREATHING NORMALLY > 35",
      "CHEST PAIN - CLAMMY",
      "CHEST PAIN - DIFFICULTY SPEAKING BETWEEN BREATHS",
      "CONVULSIONS/SEIZURES - DIABETIC - KNOWN SEIZURE DISORDER",
      "CONVULSIONS/SEIZURES - EFFECTIVE BREATHING NOT VERIFIED 35",
      "CONVULSIONS/SEIZURES - EFFECTIVE BREATHING NOT VERIFIED >35 - KNOWN SEIZURE DISORDER",
      "CONVULSIONS/SEIZURES - FOCAL/ABSENCE SEIZURE",
      "CONVULSIONS/SEIZURES - NOT SEIZING NOW - EFFECTIVE BREATHING >6 - NO SEIZURE DISORDER",
      "CVA/TIA - ABNORMAL BREATHING - CLEAR EVIDENCE - GREATER THAN 3 HRS",
      "CVA/TIA - ABNORMAL BREATHING - CLEAR EVIDENCE - LESS THAN 3 HRS",
      "CVA/TIA - ABNORMAL BREATHING - CLEAR EVIDENCE - UNKNOWN TIME",
      "CVA/TIA - ABNORMAL BREATHING - NOT TESTED - GREATER THAN 3 HRS",
      "CVA/TIA - ABNORMAL BREATHING - NOT TESTED - LESS THAN 3 HRS",
      "CVA/TIA - ABNORMAL BREATHING - NOT TESTED - UNKNOWN TIME",
      "CVA/TIA - ABNORMAL BREATHING - STRONG EVIDENCE - GREATER THAN 3 HRS",
      "CVA/TIA - ABNORMAL BREATHING - STRONG EVIDENCE - LESS THAN 3 HRS",
      "CVA/TIA - ABNORMAL BREATHING - STRONG EVIDENCE - UNKNOWN TIME",
      "CVA/TIA - NOT ALERT - CLEAR EVIDENCE - GREATER THAN 3 HRS",
      "CVA/TIA - NOT ALERT - CLEAR EVIDENCE - LESS THAN 3 HRS",
      "CVA/TIA - NOT ALERT - CLEAR EVIDENCE - UNKNOWN TIME",
      "CVA/TIA - NOT ALERT - NOT TESTED - GREATER THAN 3 HRS",
      "CVA/TIA - NOT ALERT - NOT TESTED - LESS THAN 3 HRS",
      "CVA/TIA - NOT ALERT - NOT TESTED - UNKNOWN TIME",
      "CVA/TIA - NOT ALERT - STRONG EVIDENCE - GREATER THAN 3 HRS",
      "CVA/TIA - NOT ALERT - STRONG EVIDENCE - LESS THAN 3 HRS",
      "CVA/TIA - NOT ALERT - STRONG EVIDENCE - UNKNOWN TIME",
      "CVA/TIA - SUDDEN SPEECH PROBLEMS - NOT TESTED - LESS THAN 3 HRS",
      "CVA/TIA - SUDDEN WEAKNESS OR NUMBNESS - CLEAR EVIDENCE - GREATER THAN 3 HRS",
      "CVA/TIA - SUDDEN WEAKNESS OR NUMBNESS - CLEAR EVIDENCE - LESS THAN 3 HRS",
      "CVA/TIA - SUDDEN WEAKNESS OR NUMBNESS - CLEAR EVIDENCE - UNKNOWN TIME",
      "CVA/TIA - SUDDEN WEAKNESS OR NUMBNESS - NOT TESTED - GREATER THAN 3 HRS",
      "CVA/TIA - SUDDEN WEAKNESS OR NUMBNESS - NOT TESTED - LESS THAN 3 HRS",
      "CVA/TIA - SUDDEN WEAKNESS OR NUMBNESS - NOT TESTED - UNKNOWN TIME",
      "CVA/TIA - SUDDEN WEAKNESS OR NUMBNESS - STRONG EVIDENCE - GREATER THAN 3 HRS",
      "CVA/TIA - SUDDEN WEAKNESS OR NUMBNESS - STRONG EVIDENCE - LESS THAN 3 HRS",
      "CVA/TIA - SUDDEN WEAKNESS OR NUMBNESS - STRONG EVIDENCE - UNKNOWN TIME",
      "DIABETIC - ABNORMAL BREATHING - COMBATIVE",
      "DIABETIC - ALERT AND BEHAVING NORMALLY",
      "DIABETIC - UNCONSCIOUS",
      "DOMESTIC IN PROGRESS",
      "FALLS - CHEST OR NECK INJURY WITH DIFF BREATHING - STILL ON THE GROUND",
      "FALLS - LONG FALL",
      "FALLS - LONG FALL - STILL ON THE GROUND",
      "FALLS - NON-RECENT >6",
      "FALLS - NON-RECENT >6 - STILL ON THE GROUND",
      "FALLS - NOT DANGEROUS BODY AREA",
      "FALLS - NOT DANGEROUS BODY AREA - STILL ON THE GROUND",
      "FALLS - POSSIBLY DANGEROUS BODY AREA",
      "FALLS - POSSIBLY DANGEROUS BODY AREA - STILL ON THE GROUND",
      "FALLS - PUBLIC ASSIST - NO INJURIES",
      "FALLS - PUBLIC ASSIST - NO INJURIES - STILL ON THE GROUND",
      "HAZMAT",
      "HEADACHE - BREATHING NORMALLY",
      "HEMORRHAGE/LACERATIONS - DANGEROUS HEMORRHAGE-MEDICAL",
      "HEMORRHAGE/LACERATIONS - POSSIBLY DANGEROUS HEMORRHAGE",
      "HEMORRHAGE/LACERATIONS - POSSIBLY DANGEROUS HEMORRHAGE-MEDICAL",
      "HEMORRHAGE/LACERATIONS - UNCONSCIOUS OR ARREST",
      "INTERFACILITY - ACUTE SEVERE PAIN - TRANSFER",
      "LIFTING ASSISTANCE",
      "MEDICAL",
      "MVA",
      "OB EMERGENCY - IMMINENT DELIVERY >5MO / 20WEEKS",
      "OTHER",
      "OVERDOSE/POISONING - ABNORMAL BREATHING - ACCIDENTAL",
      "OVERDOSE/POISONING - ABNORMAL BREATHING - INTENTIONAL",
      "OVERDOSE/POISONING - NOT ALERT - INTENTIONAL",
      "OVERDOSE/POISONING - UNCONSCIOUS - ACCIDENTAL",
      "OVERDOSE/POISONING - UNCONSCIOUS - INTENTIONAL",
      "PAGE CALL",
      "PAGE CALL ERWAY AMB",
      "STAB/GUNSHOT/PENETRATING TRAUMA - NON RECENT - PERIPHERAL WOUNDS - GUNSHOT",
      "PSYCHIATRIC",
      "PSYCHIATRIC / ABNORMAL BEHAVIOR - NON SUICIDAL AND ALERT",
      "PSYCHIATRIC / ABNORMAL BEHAVIOR - NON SUICIDAL AND ALERT - VIOLENT",
      "PSYCHIATRIC / ABNORMAL BEHAVIOR - NOT ALERT - VIOLENT",
      "SICK PERSON - NOT ALERT",
      "SICK PERSON - ABNORMAL BREATHING",
      "SICK PERSON - ALTERED LEVEL OF CONSCIOUSNESS",
      "SICK PERSON - NO PRIORITY SYMPTOMS - GENERAL WEAKNESS",
      "SICK PERSON - NO PRIORITY SYMPTOMS - TRANSPORTATION ONLY",
      "SICK PERSON - NO PRIORITY SYMPTOMS - UNWELL/ILL",
      "SICK PERSON - NO PRIORITY SYMPTOMS - VOMITING",
      "SICK PERSON - NO PRIORITY SYMPTOMS (COMPLAINT CONDITIONS 2-12 NOT IDENTIFIED)",
      "SMOKE INVESTIGATION",
      "STRUCTURE FIRE",
      "TRANSFER ASSIGNMENT / FIRE",
      "TRAUMATIC INJURIES - SERIOUS HEMORRHAGE",
      "TREE DOWN",
      "TRAUMATIC INJURIES - POSSIBLE DANGEROUS BODY AREA",
      "UNCONSCIOUS/FAINTING - INEFFECTIVE BREATHING FROM CASE ENTRY",
      "UNCONSCIOUS/FAINTING - NOT ALERT",
      "UNCONSCIOUS/FAINTING - UNCONSCIOUS - AGONAL/INEFFECTIVE BREATHING",
      "UNCONSCIOUS/FAINTING - UNCONSCIOUS - EFFECTIVE BREATHING",
      "UNCONSCIOUS/FAINTING - ALERT WITH ABNORMAL BREATHING",
      "UNKNOWN CONDITIONS",
      "UNKNOWN PROBLEM - MEDICAL ALARM - NO INFORMATION",
      "UNKNOWN PROBLEM - UNKNOWN STATUS",
      "VEHICLE FIRE",
      "WILD FIRE",
      "WIRES DOWN"
  );

  private static final String[] CITY_LIST = new String[]{
    "BLOSSBURG BORO",
    "ELKLAND BORO",
    "KNOXVILLE BORO",
    "LAWRENCEVILLE BORO",
    "LIBERTY BORO",
    "MANSFIELD BORO",
    "ROSEVILLE BORO",
    "TIOGA BORO",
    "WELLSBORO BORO",
    "WESTFIELD BORO",

    "BLOSS TWP",
    "BROOKFIELD TWP",
    "CHARLESTON TWP",
    "CHATHAM TWP",
    "CLYMER TWP",
    "COVINGTON TWP",
    "DEERFIELD TWP",
    "DELMAR TWP",
    "DUNCAN TWP",
    "ELK TWP",
    "FARMINGTON TWP",
    "GAINES TWP",
    "HAMILTON TWP",
    "JACKSON TWP",
    "LAWRENCE TWP",
    "LIBERTY TWP",
    "MIDDLEBURY TWP",
    "MORRIS TWP",
    "NELSON TWP",
    "OSCEOLA TWP",
    "PUTNAM TWP",
    "RICHMOND TWP",
    "RUTLAND TWP",
    "SHIPPEN TWP",
    "SULLIVAN TWP",
    "TIOGA TWP",
    "UNION TWP",
    "WARD TWP",
    "WESTFIELD TWP",

    "BLOSSBURG",
    "ELKLAND",
    "KNOXVILLE",
    "LAWRENCEVILLE",
    "LIBERTY",
    "MANSFIELD",
    "ROSEVILLE",
    "TIOGA",
    "WELLSBORO",
    "WESTFIELD",

    "BLOSS",
    "BROOKFIELD",
    "CHARLESTON",
    "CHATHAM",
    "CLYMER",
    "COVINGTON",
    "DEERFIELD",
    "DELMAR",
    "DUNCAN",
    "ELK",
    "FARMINGTON",
    "GAINES",
    "HAMILTON",
    "JACKSON",
    "LAWRENCE",
    "LIBERTY",
    "MIDDLEBURY",
    "MORRIS",
    "NELSON",
    "OSCEOLA",
    "PUTNAM",
    "RICHMOND",
    "RUTLAND",
    "SHIPPEN",
    "SULLIVAN",
    "TIOGA",
    "UNION",
    "WARD",
    "WESTFIELD",

    // Bradford County
    "CATON",
    "TOWN OF CATON",
    "WELLS TWP",

    // Potter County
    "HEBRON TWP"
  };

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CHO", "COGAN HOUSE TWP",
      "BRO", "BROWN TWP"
  });
}
