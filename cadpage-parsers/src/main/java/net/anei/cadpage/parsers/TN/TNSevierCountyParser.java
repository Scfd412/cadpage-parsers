package net.anei.cadpage.parsers.TN;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class TNSevierCountyParser extends DispatchSouthernParser {
  
  public TNSevierCountyParser() {
    super(CITY_LIST, "SEVIER COUNTY", "TN", 
          DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_X | DSFLG_ID );
    removeWords("COVE", "GATEWAY", "LA", "MALL", "PARKWAY", "ROAD");
    setupSpecialStreets("NBOUND SPUR", "PARKWAY");
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "Central_Dispatch@mydomain.com,Central_Dispatch@seviercountytn.org";
  }
  
  private static final Pattern CALL_PFX_PTN = Pattern.compile("((?:PER [ A-Z]+ )?(?:CANCEL|CXC?LD?|CSL)[/ ]*(?:ANY )?(?:FURTHER RES?PONSE|RESPONSE|ALL RESPONSE|ALL UNITS|MUTUAL AIDE RESPONSE|CALL(?: CXL| CANCEL|/FALSE)?)?(?:[ /]*(?:FALSE ALARM|False Alarm|NON-INJ|-N))?(?:[ /]*PER (?:EMS ON SCENE|ALARM CO|[^ ]+))?)[-:/ ]*");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    String callPfx = null;
    Matcher match = CALL_PFX_PTN.matcher(body);
    if (match.lookingAt()) {
      callPfx = match.group(1).trim();
      body = body.substring(match.end());
    }
    body = stripFieldStart(body, "CFS Closed:");
    body = stripFieldStart(body, "CFS Location:");
    int pt = body.indexOf(" OCA:");
    if (pt >= 0) body = body.substring(0, pt).trim(); 
    if (!super.parseMsg(body, data)) return false;
    if (callPfx != null) data.strCall = append(callPfx, " - ", data.strCall);
    data.strCity = convertCodes(data.strCity, CITY_CODES);
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " CALL";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyCancelField extends CallField {
    public MyCancelField() {
      setPattern(CALL_PFX_PTN, true);
    }
  }
  
  private static final Pattern AREA_OF_PTN = Pattern.compile("AREA(?: OF)?");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      Matcher match = CALL_PFX_PTN.matcher(field);
      if (match.lookingAt()) {
        data.strCall = match.group(1).trim();
        field = field.substring(match.end());
      }
      
      // Leading zeros are normally stripped from the beginning.  But we want to take the extra
      // step of not looking for a place field if we had to strip a leading zero.
      StartType st = StartType.START_PLACE;
      if (field.startsWith("0 ")) {
        st = StartType.START_ADDR;
        field = field.substring(2).trim();
      }
      parseAddress(st, FLAG_IGNORE_AT | FLAG_RECHECK_APT | FLAG_CROSS_FOLLOWS, field, data);
      
      if (data.strAddress.length() == 0) {
        parseAddress(data.strPlace, data);
        data.strPlace = "";
      }
      
      if (AREA_OF_PTN.matcher(data.strApt).matches()) {
        data.strPlace = append(data.strPlace, " - ", data.strApt);
        data.strApt = "";
      }
      
      data.strPlace = append(data.strPlace, " - ", getLeft());
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY";
    }
  }
  
  private class MyInfoField extends BaseInfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("http://")) return;
      super.parse(field, data);
    }
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if ( apt.startsWith("@") || 
         apt.startsWith("&") || 
         apt.startsWith("MM ") || 
         apt.endsWith(" MM") ||
         apt.equals("MM")) return true;
    return super.isNotExtraApt(apt);
  }
  
  @Override
  public String adjustMapCity(String city) {
    return city.equals("NEWPORT") ? "COSBY" : city;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "A M KING",
      "ABBEY LANE",
      "ADMIRAL FARRAGUT",
      "ALLENSVILLE ACRES",
      "ALPINE MOUNTAIN",
      "ALPINE VILLAGE",
      "AMBROSE PAINE",
      "APPLE VALLEY",
      "ARCH ROCK",
      "AREA OF JONES",
      "AUTUMN OAKS",
      "AUTUMN RIDGE",
      "AUTUMN TRAIL",
      "BABY BEAR",
      "BACK RIDGE",
      "BALL MOUNTAIN",
      "BALLS HOLLOW",
      "BARBARA LYNN",
      "BARN DOOR",
      "BASKINS CREEK",
      "BASS PRO",
      "BATES GIBSON",
      "BATTLE HILL",
      "BAUSE WATSON",
      "BAY MEADOWS",
      "BEACH FRONT",
      "BEACH VIEW",
      "BEAL WOODS",
      "BEAR CAMP",
      "BEAR COVE",
      "BEAR CREEK FALLS",
      "BEAR CROSSING",
      "BEAR HAVEN",
      "BEAVER CREEK",
      "BEECH BRANCH",
      "BELLE AIRE",
      "BERRY CLARK",
      "BERRY TRAIL",
      "BIG BEAR RIDGE",
      "BIG BUCK",
      "BIG RIVER OVERLOOK",
      "BIRD NEST",
      "BIRD RIDGE",
      "BIRDS CREEK",
      "BLACK BEAR CUB",
      "BLACK BEAR RIDGE",
      "BLACK BEAR",
      "BLACK GUM GAP",
      "BLACK OAK RIDGE",
      "BLACK OAK",
      "BLACKBERRY RIDGE",
      "BLALOCK HOLLOW",
      "BLOWING CAVE",
      "BLUE RIDGE",
      "BLUFF HEIGHTS",
      "BLUFF MOUNTAIN",
      "BLUFF RIDGE",
      "BOARDLY HILLS",
      "BOAT LAUNCH",
      "BON AIR",
      "BOYDS CREEK",
      "BRANAM HOLLOW",
      "BRYANT HOLLOW",
      "BRYCE VIEW",
      "BUCK BOARD",
      "BUCKEYE KNOB",
      "BUD REID",
      "BURDEN HILL",
      "BUTLER BRANCH",
      "BYRD HOLLOW",
      "BYRDS CROSS",
      "CABIN VILLAGE",
      "CAIN HOLLOW",
      "CAMP HOLLOW",
      "CAMPBELL LEAD",
      "CANEY CREEK",
      "CANYON HILLS",
      "CATON FARM",
      "CATONS CHAPEL",
      "CEDAR BLUFF",
      "CEDAR FALLS",
      "CENTER VIEW",
      "CHEROKEE HILLS",
      "CHEROKEE ORCHARD",
      "CHERRY BLOSSOM",
      "CHESTER MOUNTAIN",
      "CHILHOWEE SCHOOL",
      "CHRISTMAS TREE",
      "CHUCKY BURL",
      "CITY PARK",
      "CLABO MOUNTAIN",
      "COCKE COUNTY LINE",
      "CODY VIEW",
      "COMMUNITY CENTER",
      "COMMUNITY PARK",
      "CONNER HEIGHTS",
      "CONNER VIEW",
      "CONNIE HUSTON",
      "COOL CREEK",
      "COUGAR CROSSING",
      "COUNTRY OAKS",
      "COUNTY LINE",
      "COVE CREEK",
      "COVE MEADOWS",
      "COVE MOUNTAIN",
      "COVE VIEW",
      "COVE VISTA",
      "COVERED BRIDGE",
      "CREEK BEND",
      "CREEK OVERLOOK",
      "CREEK SIDE",
      "CREEKSIDE VILLAGE",
      "CROWN POINT",
      "CUMMINGS CHAPEL",
      "CYPRESS COVE",
      "DAVE SMITH",
      "DAVID LEWELLING",
      "DEEP RIVER",
      "DEER PATH",
      "DIAMOND VALLEY",
      "DIXON BRANCH",
      "DOLLY PARTON",
      "DOUBLE D",
      "DOUGLA DAM",
      "DOUGLAS DAM",
      "DUDLEY CREEK",
      "DUMPLIN CREEK TREATY",
      "DUMPLIN VALLEY",
      "EAGLE DEN",
      "ELLIS OGLE",
      "EMERALD SPRINGS",
      "EMERTS COVE",
      "ENGLE TOWN",
      "ENGLISH HILLS",
      "ERNEST MCMAHAN",
      "EVENING SPRINGS",
      "FALLEN OAK",
      "FAMILY CIRCUS",
      "FARM LAND",
      "FARMERS MOUNTAIN",
      "FERN BROOK",
      "FINE GLEN",
      "FIREFLY TRAIL",
      "FIREFLY VIEW",
      "FLAT CREEK",
      "FOREST COURT",
      "FOREST HILLS",
      "FOREST RIDGE",
      "FOREST VISTA",
      "FORGE HIDEAWAY",
      "FORKS OF THE RIVER",
      "FOX BAR",
      "FRANKE HOLLOW",
      "FRED KING",
      "FRED SALES",
      "FRENCH BROAD RIVER",
      "FRENCH BROAD",
      "FROST VALLEY",
      "GARNER HOLLOW",
      "GARY WADE",
      "GATLINBURG MOUNTAIN",
      "GATLINBURG PITTMAN",
      "GATLINBURG RIVER",
      "GEORGE DAVIS",
      "GIBSON HOLLOW",
      "GISTS CREEK",
      "GNATTY BRANCH",
      "GOLDEN HARVEST",
      "GOLF CREEK",
      "GOOSE CREEK",
      "GOOSE GAP",
      "GRASSY BRANCH",
      "GRASSY MEADOWS",
      "GRAVES DELOZIER",
      "GRAY SLATE",
      "GREEN VALLEY",
      "GREENBRIAR VILLAGE",
      "GREY FOX",
      "GREYSTONE HEIGHTS",
      "GRINDSTONE RIDGE",
      "GUFFY HOLLOW",
      "GUM STAND",
      "HAG HOLLOW",
      "HALF HIGH",
      "HAPPY HOLLOW",
      "HAPPY TRAILS",
      "HARRELL HILLS",
      "HARRISBURG MILL",
      "HATCHER MOUNTAIN",
      "HATTIE BRANCH",
      "HEAVENS PATH",
      "HELICOPTER RIDE",
      "HENDERSON CHAPEL",
      "HENDERSON SPRINGS",
      "HENRY TOWN",
      "HERMAN LARGE",
      "HERMAN SNYDER",
      "HICKORY HOLLOW",
      "HICKORY KNOLL",
      "HICKORY LODGE",
      "HICKORY TREE",
      "HIDDEN HARBOR",
      "HIDDEN HILLS",
      "HIDDEN VALLEY",
      "HIDEAWAY RIDGE",
      "HIGH ROCK",
      "HIGH VIEW",
      "HIGHLAND ACRES",
      "HILL HOLLOW",
      "HILLS CREEK",
      "HILLS GATE",
      "HILLTOP VIEW",
      "HISTORIC NATURE",
      "HODGES BEND",
      "HODGES FARM",
      "HODGES FERRY",
      "HOLLIDAY HILLS",
      "HOLLY RIDGE",
      "HOME PLACE",
      "HONEY BEE COVE",
      "HOOT OWL",
      "HUNTERS RIDGE",
      "ILLA CLYDE LANE",
      "INDIAN GAP",
      "INDIAN KNOB",
      "INDUSTRIAL PARK",
      "INGLE HOLLOW",
      "ISSAC THOMAS",
      "JACK SHARP",
      "JAKE THOMAS",
      "JAMES CREEK",
      "JAMESENA MILLER",
      "JERRY VIEW",
      "JESS WILSON",
      "JIM HICKMAN",
      "JIM PARTON",
      "JOBEY GREEN HOLLOW",
      "JOHN BARBER",
      "JOHN BOY",
      "JOHN GREEN",
      "JOHN L MARSHALL",
      "JONES COVE",
      "JUDY TOP",
      "KATY HOLLAR",
      "KELLUM CREEK",
      "KEN WADE",
      "KING BRANCH",
      "KING HOLLOW",
      "KNOB CREEK",
      "KYKER FERRY",
      "L R REAGAN",
      "LAKE VIEW",
      "LANE HOLLOW",
      "LAUGHING PINE",
      "LECONTE LANDING",
      "LECONTE VIEW",
      "LEE GREENWOOD",
      "LEISURE ACRES",
      "LEO SHARP",
      "LEWIS CLABO",
      "LEXINGTON PARK",
      "LIN CREEK",
      "LINE SPRINGS",
      "LITTLE BRANCH",
      "LITTLE COVE CHURCH",
      "LITTLE COVE",
      "LITTLE FALL",
      "LITTLE PIGEON",
      "LITTLE RIVER",
      "LITTLE VALLEY",
      "LIVE OAK",
      "LONES BRANCH",
      "LONESOME VALLEY",
      "LONG SPRINGS",
      "LORI ELLEN",
      "LOST BRANCH",
      "LOW GAP",
      "LOWE VALLEY",
      "LUMBER JACK",
      "LUTHER CLINTON",
      "MAGGIE MACK",
      "MANIS HOLLOW",
      "MAPLES BRANCH",
      "MAPLES MANOR",
      "MARIAN LAKE",
      "MARTHA MCCARTER",
      "MARY KAY",
      "MARY LEE",
      "MARY RIDGE FARM",
      "MARY ROSE",
      "MATHIS BRANCH",
      "MATTIE JANE",
      "MATTOX CEMETERY",
      "MCCARTER HOLLOW",
      "MCCARTER SISTERS",
      "MCCLEARY BEND",
      "MCCROSKEY ISLAND",
      "MEADOWLARK COVE",
      "MIDDLE CREEK",
      "MILL CREEK",
      "MILLERS RIDGE",
      "MILLICAN GROVE",
      "MINDY JO",
      "MISTY BLUE",
      "MISTY BLUFF",
      "MISTY SHADOWS",
      "MISTY VIEW",
      "MITCHELL FARM",
      "MONTE VISTA",
      "MOON HOLLOW",
      "MOOSE RIDGE",
      "MOUNTAIN BROOK",
      "MOUNTAIN LODGE",
      "MOUNTAIN MEMORIES",
      "MOUNTAIN PRESERVE",
      "MOUNTAIN SPRING",
      "MOUNTAIN VIEW",
      "MURPHYS CHAPEL",
      "MURRELL MEADOWS",
      "MUSIC MOUNTAIN",
      "MYERS HOLLOW",
      "NAILS CREEK",
      "NELL ROSE",
      "NEWELL VILLAGE",
      "NEWFOUND GAP",
      "NICHOLS BRANCH",
      "NOISY FALLS",
      "NORTHVIEW ACADEMY",
      "NOTTINGHAM HEIGHTS",
      "OAK HAVEN",
      "OAK TOP",
      "OAK TREE",
      "OGLES VIEW",
      "OLDHAM CREEK",
      "OMA LEE",
      "ONE WAY",
      "OTTO WILLIAMS",
      "OUTDOOR SPORTSMANS",
      "OUTDOOR WORLD",
      "OWLS COVE",
      "OWLS NEST",
      "PAINE LAKE",
      "PANTHER CREEK",
      "PANTHER PATH",
      "PARK HEADQUARTERS",
      "PARKSIDE VILLAGE",
      "PARTON HOLLOW",
      "PEARL VALLEY",
      "PEBBLE CREEK",
      "PHEASANT RIDGE",
      "PIGEON RIVER",
      "PINE HAVEN",
      "PINE MOUNTAIN",
      "PINE PEAK",
      "PINEY COVE",
      "PINNACLE VISTA",
      "PITTMAN CENTER",
      "PLEASANT HILL",
      "POLLARD CEMETERY",
      "POLLARD HILL",
      "PORTERFIELD GAP",
      "POWDER SPRINGS",
      "PUMPKIN PATH",
      "QUIET OAK",
      "RAIL HILL",
      "RAINBOW RIDGE",
      "RATTLESNAKE HOLLOW",
      "RAUHUFF HOLLOW",
      "RAYFIELD HOLLOW",
      "RAYS GAP",
      "REAGAN BRANCH",
      "REAGAN SPRINGS",
      "RED BANK",
      "RED BUD",
      "RED CEDAR RIDGE",
      "RICER DIVIDE",
      "RICH MOUNTAIN",
      "RICHARDSON COVE",
      "RIPPLING WATERS",
      "RIVER BANK",
      "RIVER BEND",
      "RIVER BREEZE",
      "RIVER DIVIDE",
      "RIVER MEADOWS",
      "RIVERS EDGE",
      "ROARING FORK",
      "ROBERT HENDERSON",
      "ROBINSON GAP",
      "ROCKING CHAIR",
      "ROCKY FLATS",
      "ROCKY GROVE",
      "ROCKY VALLEY",
      "ROY ELDER",
      "RUBY MAES",
      "RUSH BRANCH",
      "RUSSELL HOLLOW",
      "SAGE GRASS",
      "SAINT ANDREWS",
      "SAND PLANT",
      "SANDY BOTTOM",
      "SCENIC LOOP",
      "SCENIC MOUNTAIN",
      "SCHOOL HOUSE GAP",
      "SCOTTISH HIGHLAND",
      "SEAGLE HOLLOW",
      "SEATON SPRINGS",
      "SETTLERS VIEW",
      "SEVIER COUNTY LINE",
      "SHARP HOLLOW",
      "SHELL MOUNTAIN",
      "SHERMAN CLABO",
      "SHIELDS VIEW",
      "SHILOH CHURCH",
      "SHIRLEY MYERS",
      "SILVER PINE",
      "SINGING BIRD",
      "SINGING PINES",
      "SKI MOUNTAIN",
      "SLATE GAP",
      "SLEEPY HOLLOW",
      "SLEEPY VALLEY",
      "SMOKY MOUNTAIN VIEW",
      "SMOKY RIDGE",
      "SOURWOOD HONEY",
      "SPENCER CLACK",
      "SPORTS WORLD",
      "STARDUST MOUNTAIN",
      "STEPPING STONE",
      "STINNETT RIDGE",
      "STONE RIDGE",
      "SUGAR CAMP",
      "SUGAR HOLLOW",
      "SUGAR LOAF",
      "SUGAR MAPLE LOOP",
      "SULPHER SPRINGS",
      "SUMMIT TRAILS",
      "SUNSET GAP",
      "SUNSET HILLS",
      "SUNSET PARK",
      "TALL POPLAR",
      "TAMMY KING",
      "TATEM MARR",
      "TATTLE BRANCH",
      "TEABERRY HILL",
      "TEN POINT",
      "THE ISLAND",
      "THISTLE THORN",
      "THOMAS BYRD",
      "THOMAS CROSS",
      "THOMAS HEADRICK",
      "THOMAS LOOP",
      "TIGER LILLY",
      "TITTSWORTH SPRING",
      "TOWN OVERLOOK",
      "TOWN VIEW",
      "TRANQUILITY HILLS",
      "TREE TOP",
      "TRINITY VIEW",
      "TURTLE DOVE",
      "TWIN CITY",
      "TWIN HOLLOW",
      "TWO VIEW",
      "UNCLE HARVEY",
      "UNION HILL",
      "UNION VALLEY",
      "VALLEY ESTATES",
      "VALLEY HOME",
      "VALLEY VIEW",
      "VALLEY VISTA",
      "VALLEY WOODS",
      "VIC KING",
      "WA FLOY",
      "WALDEN COVE",
      "WALDENS CREEK",
      "WALDENS MAIN",
      "WALNUT GROVE",
      "WALNUT VISTA",
      "WALT PRICE",
      "WALTER WEBB",
      "WATSON HOLLOW",
      "WEARS MOUNTAIN",
      "WEARS OVERLOOK",
      "WEARS VALLEY",
      "WEAS VALLEY",
      "WEBB CREEK",
      "WESTGATE RESORTS",
      "WESTSIDE HILLS",
      "WHICH A",
      "WHIPOORWILL HILL",
      "WHIPPOORWILL RIDGE",
      "WHISTLING WIND",
      "WHITE OAK",
      "WHITE SCHOOL",
      "WHITES SCHOOL",
      "WILD FLOWER",
      "WILEY OAKLEY",
      "WILHITE CREEK",
      "WILL BRYAN",
      "WILLA VIEW",
      "WILLIAM HOLT",
      "WILLIAMS HOLLOW",
      "WILLOW CREEK",
      "WILLOW TRACE",
      "WILLOW TREE",
      "WILLOW WOOD",
      "WILSON HOLLOW",
      "WINDFIELD DUNN",
      "WINDSWEPT VIEW",
      "WINFEILD DUNN",
      "WINFIELD DUNN",
      "WOODS VIEW",
      "YARBERRY EDGE",
      "YELLOW BREECHES CREEK",
      "YELLOW BREECHES",
      "ZION HILL"
  };

  private static final String[] CITY_LIST = new String[]{
      "ALDER BRANCH",
      "BEECH SPRINGS",
      "BOYDS CREEK",
      "CATLETTSBURG",
      "CATON",
      "CHEROKEE HILLS",
      "COUNTRY CASCADES",
      "COSBY",
      "DUPONT",
      "GATLINBURG",
      "KODAK",
      "LAUREL",
      "NEWPORT",
      "OLDHAM",
      "PIGEON FORGE",
      "PITTMAN CENTER",
      "REAGANTOWN",
      "SEVIERVILLE",
      "SEYMOUR",
      "SHADY GROVE",
      "STRAWBERRY PLAINS",
      "TRUNDLES CROSSROADS",
      "WEARS VALLEY",
  
      "OM",
      "PIG",
      
      // Jefferson County
      "DANDRIDGE"
  };
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "OM",     "SEYOUR",
      "PIG",    "PIGEON FORGE"
  });

}
