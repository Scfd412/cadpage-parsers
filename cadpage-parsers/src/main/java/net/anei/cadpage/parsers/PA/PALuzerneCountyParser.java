package net.anei.cadpage.parsers.PA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA41Parser;

public class PALuzerneCountyParser extends DispatchA41Parser {

  public PALuzerneCountyParser() {
    super(CITY_CODES, "LUZERNE COUNTY", "PA", "[A-Z]{4}");
    setupCities(EXTRA_CITY_LIST);
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "CADDispatch@LuzerneCounty.org,";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " BORO");
    return true;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "EB I80 & MM 247",                      "+41.012133,-76.223441",
      "EB I80 & MM 247.1",                    "+41.012133,-76.223441",
      "EB I80 & MM 247.2",                    "+41.012133,-76.223441",
      "EB I80 & MM 247.3",                    "+41.012133,-76.223441",
      "EB I80 & MM 247.4",                    "+41.012133,-76.223441",
      "EB I80 & MM 247.5",                    "+41.014687,-76.210026",
      "EB I80 & MM 247.6",                    "+41.014687,-76.210026",
      "EB I80 & MM 247.7",                    "+41.014687,-76.210026",
      "EB I80 & MM 247.8",                    "+41.014687,-76.210026",
      "EB I80 & MM 247.9",                    "+41.014687,-76.210026",
      "EB I80 & MM 248",                      "+41.014687,-76.210026",
      "EB I80 & MM 248.1",                    "+41.014687,-76.210026",
      "EB I80 & MM 248.2",                    "+41.014687,-76.210026",
      "EB I80 & MM 248.3",                    "+41.014687,-76.210026",
      "EB I80 & MM 248.4",                    "+41.014687,-76.210026",
      "EB I80 & MM 248.5",                    "+41.017750,-76.191270",
      "EB I80 & MM 248.6",                    "+41.017750,-76.191270",
      "EB I80 & MM 248.7",                    "+41.017750,-76.191270",
      "EB I80 & MM 248.8",                    "+41.017750,-76.191270",
      "EB I80 & MM 248.9",                    "+41.017750,-76.191270",
      "EB I80 & MM 249",                      "+41.017750,-76.191270",
      "EB I80 & MM 249.1",                    "+41.017750,-76.191270",
      "EB I80 & MM 249.2",                    "+41.017750,-76.191270",
      "EB I80 & MM 249.3",                    "+41.017750,-76.191270",
      "EB I80 & MM 249.4",                    "+41.017750,-76.191270",
      "EB I80 & MM 249.5",                    "+41.018634,-76.172422",
      "EB I80 & MM 249.6",                    "+41.018634,-76.172422",
      "EB I80 & MM 249.7",                    "+41.018634,-76.172422",
      "EB I80 & MM 249.8",                    "+41.018634,-76.172422",
      "EB I80 & MM 249.9",                    "+41.018634,-76.172422",
      "EB I80 & MM 250",                      "+41.018634,-76.172422",
      "EB I80 & MM 250.1",                    "+41.018634,-76.172422",
      "EB I80 & MM 250.2",                    "+41.018634,-76.172422",
      "EB I80 & MM 250.3",                    "+41.018634,-76.172422",
      "EB I80 & MM 250.4",                    "+41.018634,-76.172422",
      "EB I80 & MM 250.5",                    "+41.005611,-76.164521",
      "EB I80 & MM 250.6",                    "+41.005611,-76.164521",
      "EB I80 & MM 250.7",                    "+41.005611,-76.164521",
      "EB I80 & MM 250.8",                    "+41.005611,-76.164521",
      "EB I80 & MM 250.9",                    "+41.005611,-76.164521",
      "EB I80 & MM 251",                      "+41.005611,-76.164521",
      "EB I80 & MM 251.1",                    "+41.005611,-76.164521",
      "EB I80 & MM 251.2",                    "+41.005611,-76.164521",
      "EB I80 & MM 251.3",                    "+41.005611,-76.164521",
      "EB I80 & MM 251.4",                    "+41.005611,-76.164521",
      "EB I80 & MM 251.5",                    "+41.004651,-76.146606",
      "EB I80 & MM 251.6",                    "+41.004651,-76.146606",
      "EB I80 & MM 251.7",                    "+41.004651,-76.146606",
      "EB I80 & MM 251.8",                    "+41.004651,-76.146606",
      "EB I80 & MM 251.9",                    "+41.004651,-76.146606",
      "EB I80 & MM 252",                      "+41.004651,-76.146606",
      "EB I80 & MM 252.1",                    "+41.004651,-76.146606",
      "EB I80 & MM 252.2",                    "+41.004651,-76.146606",
      "EB I80 & MM 252.3",                    "+41.004651,-76.146606",
      "EB I80 & MM 252.4",                    "+41.004651,-76.146606",
      "EB I80 & MM 252.5",                    "+41.011578,-76.129628",
      "EB I80 & MM 252.6",                    "+41.011578,-76.129628",
      "EB I80 & MM 252.7",                    "+41.011578,-76.129628",
      "EB I80 & MM 252.8",                    "+41.011578,-76.129628",
      "EB I80 & MM 252.9",                    "+41.011578,-76.129628",
      "EB I80 & MM 253",                      "+41.011578,-76.129628",
      "EB I80 & MM 253.1",                    "+41.011578,-76.129628",
      "EB I80 & MM 253.2",                    "+41.011578,-76.129628",
      "EB I80 & MM 253.3",                    "+41.011578,-76.129628",
      "EB I80 & MM 253.4",                    "+41.011578,-76.129628",
      "EB I80 & MM 253.5",                    "+41.017172,-76.112380",
      "EB I80 & MM 253.6",                    "+41.017172,-76.112380",
      "EB I80 & MM 253.7",                    "+41.017172,-76.112380",
      "EB I80 & MM 253.8",                    "+41.017172,-76.112380",
      "EB I80 & MM 253.9",                    "+41.017172,-76.112380",
      "EB I80 & MM 254",                      "+41.017172,-76.112380",
      "EB I80 & MM 254.1",                    "+41.017172,-76.112380",
      "EB I80 & MM 254.2",                    "+41.017172,-76.112380",
      "EB I80 & MM 254.3",                    "+41.017172,-76.112380",
      "EB I80 & MM 254.4",                    "+41.017172,-76.112380",
      "EB I80 & MM 254.5",                    "+41.024978,-76.096843",
      "EB I80 & MM 254.6",                    "+41.024978,-76.096843",
      "EB I80 & MM 254.7",                    "+41.024978,-76.096843",
      "EB I80 & MM 254.8",                    "+41.024978,-76.096843",
      "EB I80 & MM 254.9",                    "+41.024978,-76.096843",
      "EB I80 & MM 255",                      "+41.024978,-76.096843",
      "EB I80 & MM 255.1",                    "+41.024978,-76.096843",
      "EB I80 & MM 255.2",                    "+41.024978,-76.096843",
      "EB I80 & MM 255.3",                    "+41.024978,-76.096843",
      "EB I80 & MM 255.4",                    "+41.024978,-76.096843",
      "EB I80 & MM 255.5",                    "+41.026291,-76.078414",
      "EB I80 & MM 255.6",                    "+41.026291,-76.078414",
      "EB I80 & MM 255.7",                    "+41.026291,-76.078414",
      "EB I80 & MM 255.8",                    "+41.026291,-76.078414",
      "EB I80 & MM 255.9",                    "+41.026291,-76.078414",
      "EB I80 & MM 256",                      "+41.026291,-76.078414",
      "EB I80 & MM 256.1",                    "+41.026291,-76.078414",
      "EB I80 & MM 256.2",                    "+41.026291,-76.078414",
      "EB I80 & MM 256.3",                    "+41.026291,-76.078414",
      "EB I80 & MM 256.4",                    "+41.026291,-76.078414",
      "EB I80 & MM 256.5",                    "+41.033474,-76.061747",
      "EB I80 & MM 256.6",                    "+41.033474,-76.061747",
      "EB I80 & MM 256.7",                    "+41.033474,-76.061747",
      "EB I80 & MM 256.8",                    "+41.033474,-76.061747",
      "EB I80 & MM 256.9",                    "+41.033474,-76.061747",
      "EB I80 & MM 257",                      "+41.033474,-76.061747",
      "EB I80 & MM 257.1",                    "+41.033474,-76.061747",
      "EB I80 & MM 257.2",                    "+41.033474,-76.061747",
      "EB I80 & MM 257.3",                    "+41.033474,-76.061747",
      "EB I80 & MM 257.4",                    "+41.033474,-76.061747",
      "EB I80 & MM 257.5",                    "+41.038540,-76.043867",
      "EB I80 & MM 257.6",                    "+41.038540,-76.043867",
      "EB I80 & MM 257.7",                    "+41.038540,-76.043867",
      "EB I80 & MM 257.8",                    "+41.038540,-76.043867",
      "EB I80 & MM 257.9",                    "+41.038540,-76.043867",
      "EB I80 & MM 258",                      "+41.038540,-76.043867",
      "EB I80 & MM 258.1",                    "+41.038540,-76.043867",
      "EB I80 & MM 258.2",                    "+41.038540,-76.043867",
      "EB I80 & MM 258.3",                    "+41.038540,-76.043867",
      "EB I80 & MM 258.4",                    "+41.038540,-76.043867",
      "NB I81 & MM 145",                      "+40.972297,-76.031735",
      "NB I81 & MM 145.1",                    "+40.972297,-76.031735",
      "NB I81 & MM 145.2",                    "+40.972297,-76.031735",
      "NB I81 & MM 145.3",                    "+40.972297,-76.031735",
      "NB I81 & MM 145.4",                    "+40.972297,-76.031735",
      "NB I81 & MM 145.5",                    "+40.985609,-76.033062",
      "NB I81 & MM 145.6",                    "+40.985609,-76.033062",
      "NB I81 & MM 145.7",                    "+40.985609,-76.033062",
      "NB I81 & MM 145.8",                    "+40.985609,-76.033062",
      "NB I81 & MM 145.9",                    "+40.985609,-76.033062",
      "NB I81 & MM 146",                      "+40.985609,-76.033062",
      "NB I81 & MM 146.1",                    "+40.985609,-76.033062",
      "NB I81 & MM 146.2",                    "+40.985609,-76.033062",
      "NB I81 & MM 146.3",                    "+40.985609,-76.033062",
      "NB I81 & MM 146.4",                    "+40.985609,-76.033062",
      "NB I81 & MM 146.5",                    "+40.995524,-76.019232",
      "NB I81 & MM 146.6",                    "+40.995524,-76.019232",
      "NB I81 & MM 146.7",                    "+40.995524,-76.019232",
      "NB I81 & MM 146.8",                    "+40.995524,-76.019232",
      "NB I81 & MM 146.9",                    "+40.995524,-76.019232",
      "NB I81 & MM 147",                      "+40.995524,-76.019232",
      "NB I81 & MM 147.1",                    "+40.995524,-76.019232",
      "NB I81 & MM 147.2",                    "+40.995524,-76.019232",
      "NB I81 & MM 147.3",                    "+40.995524,-76.019232",
      "NB I81 & MM 147.4",                    "+40.995524,-76.019232",
      "SB I81 & MM 145",                      "+40.972297,-76.031735",
      "SB I81 & MM 145.1",                    "+40.972297,-76.031735",
      "SB I81 & MM 145.2",                    "+40.972297,-76.031735",
      "SB I81 & MM 145.3",                    "+40.972297,-76.031735",
      "SB I81 & MM 145.4",                    "+40.972297,-76.031735",
      "SB I81 & MM 145.5",                    "+40.985609,-76.033062",
      "SB I81 & MM 145.6",                    "+40.985609,-76.033062",
      "SB I81 & MM 145.7",                    "+40.985609,-76.033062",
      "SB I81 & MM 145.8",                    "+40.985609,-76.033062",
      "SB I81 & MM 145.9",                    "+40.985609,-76.033062",
      "SB I81 & MM 146",                      "+40.985609,-76.033062",
      "SB I81 & MM 146.1",                    "+40.985609,-76.033062",
      "SB I81 & MM 146.2",                    "+40.985609,-76.033062",
      "SB I81 & MM 146.3",                    "+40.985609,-76.033062",
      "SB I81 & MM 146.4",                    "+40.985609,-76.033062",
      "SB I81 & MM 146.5",                    "+40.995524,-76.019232",
      "SB I81 & MM 146.6",                    "+40.995524,-76.019232",
      "SB I81 & MM 146.7",                    "+40.995524,-76.019232",
      "SB I81 & MM 146.8",                    "+40.995524,-76.019232",
      "SB I81 & MM 146.9",                    "+40.995524,-76.019232",
      "SB I81 & MM 147",                      "+40.995524,-76.019232",
      "SB I81 & MM 147.1",                    "+40.995524,-76.019232",
      "SB I81 & MM 147.2",                    "+40.995524,-76.019232",
      "SB I81 & MM 147.3",                    "+40.995524,-76.019232",
      "SB I81 & MM 147.4",                    "+40.995524,-76.019232",
      "WB I80 & MM 247",                      "+41.012133,-76.223441",
      "WB I80 & MM 247.1",                    "+41.012133,-76.223441",
      "WB I80 & MM 247.2",                    "+41.012133,-76.223441",
      "WB I80 & MM 247.3",                    "+41.012133,-76.223441",
      "WB I80 & MM 247.4",                    "+41.012133,-76.223441",
      "WB I80 & MM 247.5",                    "+41.014687,-76.210026",
      "WB I80 & MM 247.6",                    "+41.014687,-76.210026",
      "WB I80 & MM 247.7",                    "+41.014687,-76.210026",
      "WB I80 & MM 247.8",                    "+41.014687,-76.210026",
      "WB I80 & MM 247.9",                    "+41.014687,-76.210026",
      "WB I80 & MM 248",                      "+41.014687,-76.210026",
      "WB I80 & MM 248.1",                    "+41.014687,-76.210026",
      "WB I80 & MM 248.2",                    "+41.014687,-76.210026",
      "WB I80 & MM 248.3",                    "+41.014687,-76.210026",
      "WB I80 & MM 248.4",                    "+41.014687,-76.210026",
      "WB I80 & MM 248.5",                    "+41.017750,-76.191270",
      "WB I80 & MM 248.6",                    "+41.017750,-76.191270",
      "WB I80 & MM 248.7",                    "+41.017750,-76.191270",
      "WB I80 & MM 248.8",                    "+41.017750,-76.191270",
      "WB I80 & MM 248.9",                    "+41.017750,-76.191270",
      "WB I80 & MM 249",                      "+41.017750,-76.191270",
      "WB I80 & MM 249.1",                    "+41.017750,-76.191270",
      "WB I80 & MM 249.2",                    "+41.017750,-76.191270",
      "WB I80 & MM 249.3",                    "+41.017750,-76.191270",
      "WB I80 & MM 249.4",                    "+41.017750,-76.191270",
      "WB I80 & MM 249.5",                    "+41.018634,-76.172422",
      "WB I80 & MM 249.6",                    "+41.018634,-76.172422",
      "WB I80 & MM 249.7",                    "+41.018634,-76.172422",
      "WB I80 & MM 249.8",                    "+41.018634,-76.172422",
      "WB I80 & MM 249.9",                    "+41.018634,-76.172422",
      "WB I80 & MM 250",                      "+41.018634,-76.172422",
      "WB I80 & MM 250.1",                    "+41.018634,-76.172422",
      "WB I80 & MM 250.2",                    "+41.018634,-76.172422",
      "WB I80 & MM 250.3",                    "+41.018634,-76.172422",
      "WB I80 & MM 250.4",                    "+41.018634,-76.172422",
      "WB I80 & MM 250.5",                    "+41.005611,-76.164521",
      "WB I80 & MM 250.6",                    "+41.005611,-76.164521",
      "WB I80 & MM 250.7",                    "+41.005611,-76.164521",
      "WB I80 & MM 250.8",                    "+41.005611,-76.164521",
      "WB I80 & MM 250.9",                    "+41.005611,-76.164521",
      "WB I80 & MM 251",                      "+41.005611,-76.164521",
      "WB I80 & MM 251.1",                    "+41.005611,-76.164521",
      "WB I80 & MM 251.2",                    "+41.005611,-76.164521",
      "WB I80 & MM 251.3",                    "+41.005611,-76.164521",
      "WB I80 & MM 251.4",                    "+41.005611,-76.164521",
      "WB I80 & MM 251.5",                    "+41.004651,-76.146606",
      "WB I80 & MM 251.6",                    "+41.004651,-76.146606",
      "WB I80 & MM 251.7",                    "+41.004651,-76.146606",
      "WB I80 & MM 251.8",                    "+41.004651,-76.146606",
      "WB I80 & MM 251.9",                    "+41.004651,-76.146606",
      "WB I80 & MM 252",                      "+41.004651,-76.146606",
      "WB I80 & MM 252.1",                    "+41.004651,-76.146606",
      "WB I80 & MM 252.2",                    "+41.004651,-76.146606",
      "WB I80 & MM 252.3",                    "+41.004651,-76.146606",
      "WB I80 & MM 252.4",                    "+41.004651,-76.146606",
      "WB I80 & MM 252.5",                    "+41.011578,-76.129628",
      "WB I80 & MM 252.6",                    "+41.011578,-76.129628",
      "WB I80 & MM 252.7",                    "+41.011578,-76.129628",
      "WB I80 & MM 252.8",                    "+41.011578,-76.129628",
      "WB I80 & MM 252.9",                    "+41.011578,-76.129628",
      "WB I80 & MM 253",                      "+41.011578,-76.129628",
      "WB I80 & MM 253.1",                    "+41.011578,-76.129628",
      "WB I80 & MM 253.2",                    "+41.011578,-76.129628",
      "WB I80 & MM 253.3",                    "+41.011578,-76.129628",
      "WB I80 & MM 253.4",                    "+41.011578,-76.129628",
      "WB I80 & MM 253.5",                    "+41.017172,-76.112380",
      "WB I80 & MM 253.6",                    "+41.017172,-76.112380",
      "WB I80 & MM 253.7",                    "+41.017172,-76.112380",
      "WB I80 & MM 253.8",                    "+41.017172,-76.112380",
      "WB I80 & MM 253.9",                    "+41.017172,-76.112380",
      "WB I80 & MM 254",                      "+41.017172,-76.112380",
      "WB I80 & MM 254.1",                    "+41.017172,-76.112380",
      "WB I80 & MM 254.2",                    "+41.017172,-76.112380",
      "WB I80 & MM 254.3",                    "+41.017172,-76.112380",
      "WB I80 & MM 254.4",                    "+41.017172,-76.112380",
      "WB I80 & MM 254.5",                    "+41.024978,-76.096843",
      "WB I80 & MM 254.6",                    "+41.024978,-76.096843",
      "WB I80 & MM 254.7",                    "+41.024978,-76.096843",
      "WB I80 & MM 254.8",                    "+41.024978,-76.096843",
      "WB I80 & MM 254.9",                    "+41.024978,-76.096843",
      "WB I80 & MM 255",                      "+41.024978,-76.096843",
      "WB I80 & MM 255.1",                    "+41.024978,-76.096843",
      "WB I80 & MM 255.2",                    "+41.024978,-76.096843",
      "WB I80 & MM 255.3",                    "+41.024978,-76.096843",
      "WB I80 & MM 255.4",                    "+41.024978,-76.096843",
      "WB I80 & MM 255.5",                    "+41.026291,-76.078414",
      "WB I80 & MM 255.6",                    "+41.026291,-76.078414",
      "WB I80 & MM 255.7",                    "+41.026291,-76.078414",
      "WB I80 & MM 255.8",                    "+41.026291,-76.078414",
      "WB I80 & MM 255.9",                    "+41.026291,-76.078414",
      "WB I80 & MM 256",                      "+41.026291,-76.078414",
      "WB I80 & MM 256.1",                    "+41.026291,-76.078414",
      "WB I80 & MM 256.2",                    "+41.026291,-76.078414",
      "WB I80 & MM 256.3",                    "+41.026291,-76.078414",
      "WB I80 & MM 256.4",                    "+41.026291,-76.078414",
      "WB I80 & MM 256.5",                    "+41.033474,-76.061747",
      "WB I80 & MM 256.6",                    "+41.033474,-76.061747",
      "WB I80 & MM 256.7",                    "+41.033474,-76.061747",
      "WB I80 & MM 256.8",                    "+41.033474,-76.061747",
      "WB I80 & MM 256.9",                    "+41.033474,-76.061747",
      "WB I80 & MM 257",                      "+41.033474,-76.061747",
      "WB I80 & MM 257.1",                    "+41.033474,-76.061747",
      "WB I80 & MM 257.2",                    "+41.033474,-76.061747",
      "WB I80 & MM 257.3",                    "+41.033474,-76.061747",
      "WB I80 & MM 257.4",                    "+41.033474,-76.061747",
      "WB I80 & MM 257.5",                    "+41.038540,-76.043867",
      "WB I80 & MM 257.6",                    "+41.038540,-76.043867",
      "WB I80 & MM 257.7",                    "+41.038540,-76.043867",
      "WB I80 & MM 257.8",                    "+41.038540,-76.043867",
      "WB I80 & MM 257.9",                    "+41.038540,-76.043867",
      "WB I80 & MM 258",                      "+41.038540,-76.043867",
      "WB I80 & MM 258.1",                    "+41.038540,-76.043867",
      "WB I80 & MM 258.2",                    "+41.038540,-76.043867",
      "WB I80 & MM 258.3",                    "+41.038540,-76.043867",
      "WB I80 & MM 258.4",                    "+41.038540,-76.043867"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{

      "10",   "BEAR CREEK VILLAGE",
      "11",   "ASHLEY",
      "12",   "AVOCA",
      "13",   "BEAR CREEK TWP",
      "14",   "BLACK CREEK TWP",
      "15",   "BUCK TWP",
      "16",   "BUTLER TWP",
      "17",   "CONYNGHAM",
      "18",   "CONYNGHAM TWP",
      "1801", "MOCANAQUA",
      "1802", "WAPWALLOPEN",
      "1803", "WAPWALLOPEN",
      "1804", "WAPWALLOPEN",
      "19",   "COURTDALE",
      "1901", "HAZLE TWP",
      "21",   "DALLAS",
      "22",   "DALLAS TWP",
      "23",   "DENNISON TWP",
      "24",   "DORRANCE TWP",
      "25",   "DUPONT",
      "2501", "HAZLE TWP",
      "26",   "DURYEA",
      "27",   "EDWARDSVILLE",
      "28",   "EXETER",
      "2801", "HAZLE TWP",
      "29",   "EXETER TWP",
      "31",   "FAIRMOUNT TWP",
      "32",   "FAIRVIEW TWP",
      "33",   "FORTY FORT",
      "34",   "FOSTER TWP",
      "3401", "HAZLE TWP",
      "35",   "FRANKLIN TWP",
      "36",   "FREELAND",
      "37",   "HANOVER TWP",
      "38",   "HARVEYS LAKE",
      "39",   "HAZLETON CITY",
      "41",   "HAZLE TWP",
      "42",   "HOLLENBACK TWP",
      "4201", "WAPWALLOPEN",
      "4202", "WAPWALLOPEN",
      "4203", "WAPWALLOPEN",
      "43",   "HUGHESTOWN",
      "44",   "HUNLOCK TWP",
      "4401", "HUNLOCK CREEK",
      "4402", "HUNLOCK CREEK",
      "4403", "HUNLOCK CREEK",
      "4404", "HUNLOCK CREEK",
      "4405", "HUNLOCK CREEK",
      "45",   "HUNTINGTON TWP",
      "4501", "SHICKSHINNY",
      "4502", "SHICKSHINNY",
      "4503", "STILLWATER",
      "4504", "SHICKSHINNY",
      "46",   "JACKSON TWP",
      "4601", "HAZLE TWP",
      "47",   "JEDDO",
      "48",   "JENKINS TWP",
      "49",   "KINGSTON",
      "51",   "KINGSTON TWP",
      "52",   "LAFLIN",
      "53",   "LAKE TWP",
      "54",   "LARKSVILLE",
      "5401", "HAZLE TWP",
      "55",   "LAUREL RUN",
      "56",   "LEHMAN TWP",
      "57",   "LUZERNE",
      "58",   "NANTICOKE CITY",
      "59",   "NESCOPECK",
      "60",   "PENN LAKE PARK",
      "61",   "NESCOPECK TWP",
      "62",   "NEW COLUMBUS",
      "63",   "NEWPORT TWP",
      "6301", "NANTICOKE",
      "6302", "NANTICOKE",
      "6303", "NANTICOKE",
      "6304", "NANTICOKE",
      "6305", "NANTICOKE",
      "6306", "GLEN LYON",
      "6308", "GLEN LYON",
      "64",   "NUANGOLA",
      "65",   "PITTSTON CITY",
      "66",   "PITTSTON TWP",
      "67",   "PLAINS TWP",
      "68",   "PLYMOUTH",
      "69",   "PLYMOUTH TWP",
      "71",   "PRINGLE",
      "72",   "RICE TWP",
      "73",   "ROSS TWP",
      "7301", "HAZLE TWP",
      "74",   "SALEM TWP",
      "7401", "BERWICK",
      "7402", "BERWICK",
      "7403", "SHICKSHINNY",
      "7404", "BERWICK",
      "7405", "BERWICK",
      "7406", "BERWICK",
      "75",   "SHICKSHINNY",
      "7501", "SHICKSHINNY",
      "76",   "SLOCUM TWP",
      "7601", "WAPWALLOPEN",
      "77",   "SUGARLOAF TWP",
      "78",   "SUGAR NOTCH",
      "79",   "SWOYERSVILLE",
      "81",   "UNION TWP",
      "8101", "SHICKSHINNY",
      "8102", "SHICKSHINNY",
      "8103", "SHICKSHINNY",
      "8104", "SHICKSHINNY",
      "8105", "SHICKSHINNY",
      "8106", "SHICKSHINNY",
      "8107", "SHICKSHINNY",
      "82",   "WARRIOR RUN",
      "83",   "WEST HAZLETON",
      "84",   "WEST PITTSTON",
      "85",   "WEST WYOMING",
      "86",   "WHITE HAVEN",
      "87",   "WILKES BARRE CITY",
      "88",   "WILKES BARRE TWP",
      "89",   "WRIGHT TWP",
      "91",   "WYOMING",
      "9101", "HAZLE TWP",
      "92",   "YATESVILLE",
      "93",   "EAST SIDE",
      "94",   "MONROE TWP"

  });

  private static final String[] EXTRA_CITY_LIST = new String[]{

      // Carbon County
      "CARBON CO",
      "BANKS",
      "BANKS TWP",
      "BEAVER MEADOWS",
      "EAST SIDE",
      "EAST SIDE BORO",
      "KIDDER",
      "KIDDER TWP",
      "LAUSANNE",
      "LAUSANNE TWP",
      "LEHIGH",
      "LEHIGH TWP",
      "PACKER",
      "PACKER TWP",
      "TRESCKOW",
      "WEATHERLY",

      // Columbia County
      "COLUMBIA CO",
      "BEAVER",
      "BEAVER TWP",
      "BENTON",
      "BENTON TWP",
      "BERWICK",
      "BRIAR CREEK",
      "BRIAR CREEK TWP",
      "FISHING CREEK",
      "FISHING CREEK TWP",
      "FOUNDRYVILLE",
      "JAAMISON",
      "JAMISON CITY",
      "JONESTOWN",
      "MIFFLIN",
      "MIFFLIN TWP",
      "MIFFLINVILLE",
      "STILLWATER",
      "SOUTH CENTRE",
      "SUGARLOAF",
      "SUGARLOAF TWP",

      // Lackawanna County
      "LACKAWANNA CO",
      "CLIFTON",
      "CLIFTON TWP",
      "NEWTON",
      "NEWTON TWP",
      "MOOSIC",
      "OLDFORGE",
      "OLD FORGE",
      "RANSOM",
      "RANSOM TWP",
      "SCRANTON",
      "SPRINGBROOK",
      "SPRINGBROOK TWP",
      "THORNHURST",
      "THORNHURST TWP",

      // Monroe County
      "MONROE CO",
      "TOBYHANNA",
      "TOBYHANNA TWP",

      // Schuylkill County
      "SCHUYLKILL CO",
      "KLINE",
      "KLINE TWP",
      "MCADOO",
      "NORTH UNION",
      "NORTH UNION TWP",
      "NUREMBERG",
      "ONEIDA",
      "EAST UNION",
      "EAST UNION TWP",

      // Sullivan County
      "SULLIVAN CO",
      "DAVIDSON",
      "DAVIDSON TWP",

      // Wyoming County
      "WYOMING CO",
      "EXTER",
      "EXETER TWP",
      "FALLS",
      "FALLS TWP",
      "FORKSTON",
      "FORKSTON TWP",
      "MONROE",
      "MONROE TWP",
      "NORTH MORELAND",
      "NORTH MORELAND TWP",
      "NORTHMORELAND",
      "NORTHMORELAND TWP",
      "NOXEN",
      "NOXEN TWP"
  };
}
