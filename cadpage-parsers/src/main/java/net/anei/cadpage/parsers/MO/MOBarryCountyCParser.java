package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class MOBarryCountyCParser extends DispatchSPKParser {
  public MOBarryCountyCParser() {
    this("BARRY COUNTY", "MO");
  }
  
  MOBarryCountyCParser(String defCity, String defState) {
    super(defCity, defState);
    setupGpsLookupTable(MOBarryCountyParser.GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "dispatch@barrycountye911.org";
  }
  
  @Override
  protected String adjustGpsLookupAddress(String address, String apt) {
    return MOBarryCountyParser.fixGpsLookupAddress(address, apt);
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (data.strCity.equalsIgnoreCase("COUNTY")) data.strCity = "";
    return true;
  }
  
}
