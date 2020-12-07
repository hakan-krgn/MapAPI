# MapAPI

**Example Code:**

```java
MapAPI.MapManager mapManager = MapAPI.getMapManager();

mapManager.setImage("https://minotar.net/avatar/blueybighats/64.png", 0, 0, false);
mapManager.setImage("https://minotar.net/avatar/Mr_Obliviate/64.png", 64, 0, false);
mapManager.setImage("https://minotar.net/avatar/AhmetOzkaan/64.png", 0, 64, false);
mapManager.setImage("https://minotar.net/avatar/mSquid_/64.png", 64, 64, false);

mapManager.addText("xxxxxxx", new Color(17, 49, 227), true, 55);

HMap hMap = mapManager.create();

hMap.open(Bukkit.getPlayer("blueybighats"), 0);
```

https://user-images.githubusercontent.com/42969966/101343036-74279b00-3894-11eb-8d5a-e6b3442990c6.PNG
