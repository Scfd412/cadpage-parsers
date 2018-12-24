package net.anei.cadpage.parsers.OR;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class ORClatsopCountyParser extends FieldProgramParser {

  public ORClatsopCountyParser() {
    super(CITY_LIST, "CLATSOP COUNTY", "OR", 
          "ADDR! ( CITY | ADDR2/Z CITY | ADDR2? ) CALL/SLS+ ");
    setupSpecialStreets("PROMENADE");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "cmireporting@astoria.or.us,lewisandclarkfd@astoria.or.us";
  }

  private static final Pattern PROM_PTN = Pattern.compile("\\bPROM\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    //require subject
    if (subject.length() == 0) return false;
    data.strCall = subject;
    
    // Expand PROM -> PROMONADE
    subject = PROM_PTN.matcher(subject).replaceAll("PROMENADE");
    body = PROM_PTN.matcher(body).replaceAll("PROMENADE");
    if (!parseFields(body.split("/"), data)) return false;
    
    // Try to rule out random messages containing a slash
    if (data.strCity.length() > 0) return true;
    for (String call : data.strCall.split("/")) {
      if (CALL_SET.contains(call.trim().toUpperCase())) return true;
    }
    return false;
  }
  
  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR2")) return new MyAddress2Field();
    return super.getField(name);
  }
  
  private static final Pattern AVE_ST_PTN = Pattern.compile("(?:AVE|AVENUE) +[A-Z]", Pattern.CASE_INSENSITIVE);
  private class MyAddress2Field extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (AVE_ST_PTN.matcher(field).matches() || isValidAddress(field)) {
        parse(field, data);
        return true;
      } else {
        return false;
      }
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strAddress = append(data.strAddress, " & ", field);
    }
  }
  
  @Override
  protected String adjustGpsLookupAddress(String address) {
    address = address.toUpperCase();
    address = address.replace(" / MP ", " MP ");
    return address;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "26 101 JCT",                           "+45.939363,-123.919623",
      "10TH ST ACCESS",                       "+46.030907,-123.929361",
      "12TH AND PROM",                        "+46.001274,-123.927432",
      "26 53 JCT",                            "+45.902153,-123.759972",
      "ARCADIA BEACH",                        "+45.846305,-123.960264",
      "ARCH CAPE",                            "+45.804352,-123.966011",
      "ARCH CAPE TUNNEL",                     "+45.802044,-123.965288",
      "BROADWAY FIELD",                       "+45.993512,-123.916551",
      "BROADWAY PARK",                        "+45.993425,-123.915562",
      "CARNAHAN",                             "+46.095082,-123.907797",
      "CARTWRIGHT",                           "+45.981723,-123.928338",
      "CB SEMAPHORE 1",                       "+45.858849,-123.964180",
      "CB SEMAPHORE 2",                       "+45.865637,-123.963159",
      "CB SEMAPHORE 3",                       "+45.872553,-123.963135",
      "CB SEMAPHORE 4",                       "+45.879781,-123.963943",
      "CB SEMAPHORE 5",                       "+45.889449,-123.965186",
      "CB SEMAPHORE 6",                       "+45.892885,-123.964964",
      "CB SEMAPHORE 7",                       "+45.897006,-123.965320",
      "CB SEMAPHORE 8",                       "+45.899077,-123.965336",
      "CB SEMAPHORE 9",                       "+45.901844,-123.965805",
      "CB SEMAPHORE 10",                      "+45.907103,-123.967697",
      "CHAMBERS",                             "+45.993687,-123.919783",
      "CHAPMAN PT",                           "+45.907153,-123.965168",
      "CRESENT BEACH",                        "+45.912458,-123.970155",
      "CROWN CAMP",                           "+46.016022,-123.870964",
      "CULLABY LAKE N",                       "+46.085188,-123.908563",
      "CULLABY LAKE S",                       "+46.080237,-123.906402",
      "DEL REY BEACH APPROACH",               "+46.048530,-123.930738",
      "ECOLA STATE PARK",                     "+45.919356,-123.974238",
      "END OF THE WORLD",                     "+46.007880,-123.929106",
      "ESTUARY",                              "+46.013201,-123.921485",
      "FT CLATSOP RD MP 3.5",                 "+46.103950,-123.858170",
      "GOODMAN PARK",                         "+46.001394,-123.923170",
      "HAYSTACK ROCK",                        "+45.884353,-123.967579",
      "HELICOPTER PAD",                       "+45.973247,-123.926558",
      "HIKERS CABINS",                        "+45.941253,-123.984265",
      "HUG POINT",                            "+45.828324,-123.961312",
      "HWY 101 MP 5",                         "+46.175780,-123.869270",
      "HWY 101 MP 6",                         "+45.908820,-123.828680",
      "HWY 101 MP 7",                         "+46.158890,-123.903140",
      "HWY 101 MP 8",                         "+46.148230,-123.916850",
      "HWY 101 MP 9",                         "+46.135490,-123.927170",
      "HWY 101 MP 10",                        "+46.121720,-123.926180",
      "HWY 101 MP 15",                        "+46.079510,-123.916420",
      "HWY 101 MP 16",                        "+46.065470,-123.916170",
      "HWY 101 MP 17",                        "+46.050950,-123.914320",
      "HWY 101 MP 18",                        "+46.036080,-123.913320",
      "HWY 101 MP 19",                        "+46.021600,-123.911530",
      "HWY 101 MP 21",                        "+45.994460,-123.919870",
      "HWY 101 MP 22",                        "+45.982290,-123.926300",
      "HWY 101 MP 23",                        "+45.967970,-123.924740",
      "HWY 101 MP 24",                        "+45.954110,-123.926250",
      "HWY 101 MP 25",                        "+46.008450,-123.914650",
      "HWY 101 MP 26",                        "+45.929650,-123.928233",
      "HWY 101 MP 27",                        "+45.917178,-123.951890",
      "HWY 101 MP 28",                        "+45.907574,-123.952500",
      "HWY 101 MP 29",                        "+45.895120,-123.955110",
      "HWY 101 MP 30",                        "+45.881210,-123.958090",
      "HWY 101 MP 31",                        "+45.867320,-123.958510",
      "HWY 101 MP 32",                        "+45.854170,-123.960930",
      "HWY 101 MP 33",                        "+45.839580,-123.958000",
      "HWY 101 MP 34",                        "+45.826000,-123.960760",
      "HWY 101 MP 35",                        "+45.812020,-123.961410",
      "HWY 101 MP 36",                        "+45.798220,-123.966990",
      "HWY 101 MP 37",                        "+45.785530,-123.958470",
      "HWY 101 MP 38",                        "+45.775390,-123.945830",
      "HWY 101 MP 39",                        "+45.763640,-123.955120",
      "HWY 101 MP 40",                        "+45.752600,-123.963740",
      "HWY 101 MP 41",                        "+45.741580,-123.953200",
      "HWY 101 MP 42",                        "+45.735400,-123.934270",
      "HWY 101 MP 43",                        "+45.722290,-123.928300",
      "HWY 101 MP 44",                        "+45.717360,-123.913580",
      "HWY 101 MP 45",                        "+45.717560,-123.895740",
      "HWY 101 MP 46",                        "+45.706340,-123.885560",
      "HWY 101 MP 47",                        "+45.695490,-123.878550",
      "HWY 101B MP 2",                        "+46.148380,-123.901910",
      "HWY 101B MP 3",                        "+46.143970,-123.885610",
      "HWY 101B MP 4",                        "+46.146630,-123.867980",
      "HWY 101B MP 5",                        "+46.152350,-123.855020",
      "HWY 101B MP 6",                        "+46.167970,-123.886100",
      "HWY 103 MP 1",                         "+45.922910,-123.493910",
      "HWY 103 MP 2",                         "+45.912270,-123.504460",
      "HWY 103 MP 3",                         "+45.898770,-123.505570",
      "HWY 103 MP 4",                         "+45.896930,-123.520500",
      "HWY 103 MP 5",                         "+45.897660,-123.539480",
      "HWY 103 MP 6",                         "+45.886660,-123.544680",
      "HWY 103 MP 7",                         "+45.881270,-123.561590",
      "HWY 103 MP 8",                         "+45.869290,-123.560580",
      "HWY 202 MP 1",                         "+46.176020,-123.849740",
      "HWY 202 MP 5",                         "+46.143950,-123.801990",
      "HWY 202 MP 10",                        "+46.098030,-123.749470",
      "HWY 202 MP 15",                        "+46.060570,-123.680550",
      "HWY 202 MP 20",                        "+46.011080,-123.619990",
      "HWY 202 MP 25",                        "+45.956960,-123.578060",
      "HWY 202 MP 26",                        "+45.949050,-123.561140",
      "HWY 202 MP 27",                        "+45.945630,-123.541620",
      "HWY 202 MP 28",                        "+45.938150,-123.525850",
      "HWY 202 MP 29",                        "+45.934050,-123.509230",
      "HWY 202 MP 30",                        "+45.935780,-123.488770",
      "HWY 202 MP 31",                        "+45.943690,-123.474450",
      "HWY 202 MP 32",                        "+45.951280,-123.461560",
      "HWY 202 MP 33",                        "+45.947000,-123.443690",
      "HWY 202 MP 35",                        "+45.969120,-123.420910",
      "HWY 202 MP 40",                        "+45.981790,-123.345980",
      "HWY 202 MP 45",                        "+46.003160,-123.276610",
      "HWY 203 MP 34",                        "+45.958160,-123.431020",
      "HWY 26 MP 1",                          "+45.932340,-123.906930",
      "HWY 26 MP 2",                          "+45.921360,-123.895180",
      "HWY 26 MP 3",                          "+45.909900,-123.884390",
      "HWY 26 MP 4",                          "+45.901520,-123.867770",
      "HWY 26 MP 5",                          "+45.903740,-123.848020",
      "HWY 26 MP 7",                          "+45.908620,-123.808390",
      "HWY 26 MP 8",                          "+45.907740,-123.788610",
      "HWY 26 MP 9",                          "+45.904720,-123.768350",
      "HWY 26 MP 10",                         "+45.905170,-123.749490",
      "HWY 26 MP 11",                         "+45.907180,-123.730180",
      "HWY 26 MP 12",                         "+45.907750,-123.713820",
      "HWY 26 MP 13",                         "+45.910780,-123.695190",
      "HWY 26 MP 14",                         "+45.899540,-123.682510",
      "HWY 26 MP 15",                         "+45.903160,-123.665540",
      "HWY 26 MP 16",                         "+45.895980,-123.647780",
      "HWY 26 MP 17",                         "+45.889870,-123.628600",
      "HWY 26 MP 18",                         "+45.884950,-123.609830",
      "HWY 26 MP 19",                         "+45.872670,-123.601030",
      "HWY 26 MP 20",                         "+45.863770,-123.585720",
      "HWY 26 MP 21",                         "+45.865110,-123.567610",
      "HWY 26 MP 22",                         "+45.858350,-123.551010",
      "HWY 26 MP 23",                         "+45.849680,-123.537300",
      "HWY 26 MP 24",                         "+45.843270,-123.522690",
      "HWY 26 MP 25",                         "+45.834240,-123.507620",
      "HWY 26 MP 26",                         "+45.825760,-123.494270",
      "HWY 26 MP 27",                         "+45.812640,-123.488050",
      "HWY 26 MP 28",                         "+45.802530,-123.472940",
      "HWY 26 MP 29",                         "+45.793020,-123.457670",
      "HWY 26 MP 30",                         "+45.785090,-123.440930",
      "HWY 26 MP 31",                         "+45.778200,-123.423790",
      "HWY 26 MP 35",                         "+45.780670,-123.346900",
      "HWY 26 MP 40",                         "+45.739630,-123.266110",
      "HWY 26 MP 45",                         "+45.686700,-123.204730",
      "HWY 30 MP 50",                         "+46.099890,-122.995530",
      "HWY 30 MP 55",                         "+46.097020,-123.075090",
      "HWY 30 MP 60",                         "+46.103410,-123.174170",
      "HWY 30 MP 65",                         "+46.116500,-123.266590",
      "HWY 30 MP 70",                         "+46.125750,-123.365830",
      "HWY 30 MP 74",                         "+46.160690,-123.430910",
      "HWY 30 MP 75",                         "+46.170520,-123.440850",
      "HWY 30 MP 76",                         "+46.169870,-123.460610",
      "HWY 30 MP 77",                         "+46.168230,-123.480360",
      "HWY 30 MP 78",                         "+46.174520,-123.497950",
      "HWY 30 MP 79",                         "+46.176240,-123.517710",
      "HWY 30 MP 80",                         "+46.175020,-123.539860",
      "HWY 30 MP 81",                         "+46.170700,-123.560290",
      "HWY 30 MP 82",                         "+46.168320,-123.581120",
      "HWY 30 MP 83",                         "+46.167100,-123.601330",
      "HWY 30 MP 84",                         "+46.165540,-123.624330",
      "HWY 30 MP 85",                         "+46.164270,-123.643790",
      "HWY 30 MP 86",                         "+46.167590,-123.663610",
      "HWY 30 MP 87",                         "+46.169880,-123.684120",
      "HWY 30 MP 88",                         "+46.167770,-123.699870",
      "HWY 30 MP 89",                         "+46.169860,-123.718150",
      "HWY 30 MP 91",                         "+46.169790,-123.716150",
      "HWY 30 MP 92",                         "+46.169520,-123.739500",
      "HWY 30 MP 93",                         "+46.169580,-123.756280",
      "HWY 30 MP 94",                         "+46.183370,-123.750550",
      "HWY 30 MP 95",                         "+46.193840,-123.763530",
      "HWY 53 MP 1",                          "+45.890150,-123.758190",
      "HWY 53 MP 2",                          "+45.876960,-123.761740",
      "HWY 53 MP 3",                          "+45.865240,-123.766030",
      "HWY 53 MP 4",                          "+45.855040,-123.766360",
      "HWY 53 MP 5",                          "+45.843080,-123.768600",
      "HWY 53 MP 6",                          "+45.832380,-123.777720",
      "HWY 53 MP 7",                          "+45.822360,-123.771750",
      "HWY 53 MP 8",                          "+45.809610,-123.774670",
      "HWY 53 MP 9",                          "+45.802090,-123.791060",
      "HWY 53 MP 10",                         "+45.799440,-123.810160",
      "HWY 53 MP 11",                         "+45.787540,-123.818350",
      "HWY 53 MP 12",                         "+45.775790,-123.828970",
      "HWY 53 MP 13",                         "+45.765550,-123.838420",
      "HWY 53 MP 14",                         "+45.752540,-123.836840",
      "HWY 53 MP 15",                         "+45.741630,-123.847120",
      "HWY 53 MP 16",                         "+45.732420,-123.856500",
      "HWY 53 MP 17",                         "+45.718700,-123.852420",
      "HWY 53 MP 18",                         "+45.708260,-123.864220",
      "HWY 53 MP 19",                         "+45.701160,-123.879740",
      "INDIAN BEACH",                         "+45.930880,-123.978547",
      "JAVA REEF",                            "+46.014076,-123.911163",
      "LC RD MP 2.5",                         "+46.122330,-123.855250",
      "LC RD MP 3",                           "+46.113970,-123.854890",
      "LC RD MP 4",                           "+46.101430,-123.854040",
      "LC RD MP 5",                           "+46.087810,-123.857520",
      "LC RD MP 6.5",                         "+46.071360,-123.843420",
      "LC RD MP 7",                           "+46.064650,-123.843930",
      "LC RD MP 8",                           "+46.052680,-123.850270",
      "LC RD MP 9",                           "+46.040510,-123.857060",
      "LC RD MP 10",                          "+46.026910,-123.865090",
      "LC RD MP 11",                          "+46.014810,-123.872890",
      "LC RD MP 12",                          "+46.010500,-123.887150",
      "LC RD MP 13",                          "+46.010430,-123.903260",
      "LC ROAD  MP 5.5",                      "+46.081950,-123.852570",
      "LITTLE BEACH",                         "+46.017184,-123.923574",
      "LOGAN RD MP 1",                        "+46.096460,-123.836550",
      "LOGAN RD MP 2",                        "+46.083500,-123.833480",
      "LOGAN RD MP 3",                        "+46.073380,-123.843260",
      "LOWER NEHALEM RD MP 1",                "+45.698960,-123.824690",
      "LOWER NEHALEM RD MP 5",                "+45.704850,-123.757330",
      "LOWER NEHALEM RD MP 10",               "+45.738670,-123.717780",
      "LOWER NEHALEM RD MP 15",               "+45.765440,-123.635800",
      "LOWER NEHALEM RD MP 20",               "+45.800640,-123.617190",
      "LOWER NEHALEM RD MP 21",               "+45.814350,-123.612140",
      "LOWER NEHALEM RD MP 22",               "+45.819800,-123.601610",
      "LOWER NEHALEM RD MP 23",               "+45.833580,-123.601730",
      "LOWER NEHALEM RD MP 24",               "+45.842440,-123.593930",
      "LOWER NEHALEM RD MP 25",               "+45.854210,-123.584100",
      "OCEANWAY",                             "+45.993908,-123.926875",
      "PACIFIC WAY TRAIL",                    "+46.023965,-123.926495",
      "PETERSON PT",                          "+45.958345,-123.926774",
      "QUATAT",                               "+45.993750,-123.924975",
      "SADDLE MTN",                           "+45.962783,-123.689950",
      "SEASIDE AIRPORT",                      "+46.016236,-123.909525",
      "SEASIDE BEACH NORTH",                  "+45.999010,-123.931418",
      "SEASIDE BEACH SOUTH",                  "+45.985881,-123.935531",
      "SEASIDE LIFEGUARD TOWER",              "+45.993116,-123.931765",
      "SELTZER",                              "+45.975545,-123.939096",
      "SEMAPHORE 3",                          "+45.995780,-123.931506",
      "SEMAPHORE 6",                          "+45.997783,-123.931323",
      "SEMAPHORE 9",                          "+45.999784,-123.930960",
      "SEMAPHORE 12",                         "+46.001600,-123.930563",
      "SEMAPHORE 16",                         "+46.004505,-123.929700",
      "SEMAPHORE G",                          "+45.990460,-123.932806",
      "SEMAPHORE K",                          "+45.987900,-123.933900",
      "SEMAPHORE N",                          "+45.985822,-123.934196",
      "SEMAPHORE S",                          "+45.983685,-123.934853",
      "SEMAPHORE U",                          "+45.980812,-123.935788",
      "SILVER PT",                            "+45.857179,-123.962980",
      "SKATE PARK",                           "+45.993444,-123.917151",
      "SUNSET BEACH ACCESS",                  "+46.098710,-123.942332",
      "SURFERS PARKING LOT",                  "+45.975218,-123.943196",
      "THE COVE",                             "+45.975550,-123.939585",
      "THE GRANGE",                           "+46.097727,-123.916567",
      "THE POINT",                            "+45.975544,-123.950982",
      "THOMPSON FALLS",                       "+46.009912,-123.894361",
      "TILLAMOOK HEAD",                       "+45.965048,-123.967060",
      "TILLAMOOK HEAD PARKING",               "+45.973569,-123.953922",
      "TOLOVANA",                             "+45.872515,-123.961937",
      "TURNAROUND",                           "+45.993175,-123.930229"
  });
  
  private static final Set<String> CALL_SET = new HashSet<String>(Arrays.asList(
      "1 VEH MVA",
      "2 VEH MVA",
      "ARCING TRANSFORMERS",
      "ALARM ACTIVATION",
      "ALARM FIRE",
      "ASSIST RENDERED",
      "BACK PAIN",
      "BLADDER INF",
      "BRUSH FIRE",
      "CHEMICAL",
      "CHIM FIRE",
      "CHIMNEY FIRE",
      "DIFFICULTY BREATHING",
      "DISTURBANCE",
      "ELDERLY FALL PT HEAD INJ",
      "EMERG MEDICAL RESPONSE",
      "EMS",
      "EXTRICATION",
      "FALL PT",
      "FIRE",
      "FIRE ALARM",
      "FIRE CALL",
      "FIRE INVESTIGATION",
      "GEN MED ALARM",
      "GRASS",
      "HEAD INJ",
      "INFANT LOCKED IN VEH",
      "INFORMATION",
      "LIFT ASSIST",
      "LINE DOWN",
      "LIVE POWER LINE DOWN",
      "LOW 02",
      "MALE NOT BREATHING",
      "MEDICAL",
      "MEDICAL CALL",
      "MOTOR VEH ACCIDENT",
      "MVA",
      "MVA UNK INJ",
      "ODOR GAS",
      "OTHER ALL",
      "POSS CODE 99",
      "POSS STROKE",
      "PUBLIC ASSIST",
      "RESCUE CALL",
      "SEIZURE",
      "SEIZURE PATIENT",
      "SMOKE",
      "STRUC FIRE 1ST",
      "SUSP CIRCUMSTANCES",
      "SVR ABD PAIN",
      "TEST",
      "TEST CALL TEST CALL",
      "TRAFFIC ROADS",
      "TREE DOWN",
      "TREE DOWN IN ROADWAY",
      "TREE FALL ON HOME",
      "TWO VEHICL AIC",
      "UNK MED",
      "UNK TYP UTIL POLE DOWN",
      "UNRESP",
      "VEH FIRE",
      "VEHICLE FIRE",
      "WATER FLOW ALARM",
      "WATER RESCUE",
      "WELFARE CHECK"
  ));
  
  private static final String[] CITY_LIST = new String[]{
      
      // Cities
      "ASTORIA",
      "CANNON BEACH",
      "GEARHART",
      "SEASIDE",
      "WARRENTON",

      // Census-designated places
      "JEFFERS GARDEN",
      "WESTPORT",

      // Unincorporated communities
      "ARCH CAPE",
      "BRADWOOD",
      "BROWNSMEAD",
      "CARNAHAN",
      "CLIFTON",
      "ELSIE",
      "FERN HILL",
      "FORT STEVENS",
      "GRAND RAPIDS",
      "HAMLET",
      "HAMMOND",
      "JEWELL",
      "JEWELL JUNCTION",
      "KNAPPA",
      "LUKARILLA",
      "MELVILLE",
      "MILES CROSSING",
      "MISHAWAKA",
      "NAVY HEIGHTS",
      "NECANICUM",
      "OKLAHOMA HILL",
      "OLNEY",
      "SKIPANON",
      "SUNSET BEACH",
      "SURF PINES",
      "SVENSEN",
      "SVENSEN JUNCTION",
      "TAYLORVILLE",
      "TOLOVANA PARK",
      "TONGUE POINT VILLAGE",
      "UNIONTOWN",
      "VESPER",
      "VINEMAPLE",
      "WAUNA"
  };
}
