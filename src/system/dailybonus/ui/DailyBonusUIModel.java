package system.dailybonus.ui;

import commands.Type;
import lobby.LobbyManager;
import system.dailybonus.BonusListItem;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DailyBonusUIModel {
   public void showBonuses(LobbyManager lobby, List bonusesData) {
      JSONObject json = new JSONObject();
      JSONArray items = new JSONArray();
      Iterator var6 = bonusesData.iterator();

      while(var6.hasNext()) {
         BonusListItem item = (BonusListItem)var6.next();
         JSONObject _item = new JSONObject();
         _item.put("id", item.getBonus().id);
         _item.put("count", Integer.valueOf(item.getCount()));
         items.add(_item);
      }

      json.put("items", items);
      lobby.send(Type.LOBBY, new String[]{"show_bonuses", json.toJSONString()});
   }

   public void showCrystalls(LobbyManager lobby, int count) {
      lobby.send(Type.LOBBY, new String[]{"show_crystalls", String.valueOf(count)});
   }

   public void showNoSupplies() {
   }
}
