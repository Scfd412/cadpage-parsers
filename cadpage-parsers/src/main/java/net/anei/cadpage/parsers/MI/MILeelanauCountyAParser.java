package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class MILeelanauCountyAParser extends DispatchA19Parser {
  
  public MILeelanauCountyAParser() {
    super(MILeelanauCountyParser.CITY_CODES, "LEELANAU COUNTY", "MI");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Rapid Notifications")) return false;
    return super.parseMsg("", body, data);
  }

  @Override
  public String getFilter() {
    return "outgoing@co.leelanau.mi.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
