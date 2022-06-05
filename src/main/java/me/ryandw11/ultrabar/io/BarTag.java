package me.ryandw11.ultrabar.io;

import me.ryandw11.ods.ODS;
import me.ryandw11.ods.tags.*;
import me.ryandw11.ultrabar.api.BossBarBuilder;
import me.ryandw11.ultrabar.api.UBossBar;
import me.ryandw11.ultrabar.api.enums.CountStyle;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a Bar so that ODS can save it to a file.
 */
public class BarTag extends ObjectTag {

    /**
     * Create the BarTag from a UBossBar.
     *
     * @param bar The bar to create the BarTag from.
     */
    public BarTag(UBossBar bar) {
        super(bar.getPID().toString());

        this.addTag(new IntTag("id", bar.getId()));
        this.addTag(new StringTag("message", bar.getMessage()));
        this.addTag(new StringTag("color", bar.getColor().toString()));
        this.addTag(new StringTag("style", bar.getStyle().toString()));
        this.addTag(new IntTag("time", bar.getTime()));
        this.addTag(new DoubleTag("progress", bar.getProgress()));
        if (bar.getWorld() != null)
            this.addTag(new StringTag("world", bar.getWorld().getName()));
        this.addTag(new ByteTag("tracked", (byte) (bar.isTracked() ? 1 : 0)));
        this.addTag(new ByteTag("public", (byte) (bar.isPublicBar() ? 1 : 0)));
        Map<String, StringTag> map = new HashMap<>();
        for (Map.Entry<String, String> mp : bar.getData().entrySet()) {
            map.put(mp.getKey(), new StringTag(mp.getKey(), mp.getValue()));
        }
        this.addTag(new MapTag<>("data", map));
        if (bar.getPermission().isPresent())
            this.addTag(new StringTag("permission", bar.getPermission().get()));
        this.addTag(new ByteTag("hasTimer", (byte) (bar.getTimer().isPresent() ? 1 : 0)));
        this.addTag(new ByteTag("countStyle", (byte) (bar.getCountStyle() == CountStyle.UP ? 0 : 1)));

        this.addTag(ODS.wrap("ver", 2));
    }

    /**
     * Create the bar tag from an object tag.
     *
     * @param objectTag The object tag to create a bar tag from.
     */
    public BarTag(ObjectTag objectTag) {
        super(objectTag.getName());
        this.setValue(objectTag.getValue());
    }

    /**
     * Build the boss bar from the file.
     *
     * @return The boss bar from the file.
     */
    public UBossBar buildBossBar() {
        int id = ODS.unwrap((IntTag) getTag("id"));
        String message = ODS.unwrap((StringTag) getTag("message"));
        BarColor barColor = BarColor.valueOf(ODS.unwrap((StringTag) getTag("color")));
        BarStyle barStyle = BarStyle.valueOf(ODS.unwrap((StringTag) getTag("style")));
        int time = ODS.unwrap((IntTag) getTag("time"));
        double progress = ODS.unwrap((DoubleTag) getTag("progress"));
        World w = null;
        if (hasTag("world"))
            w = Bukkit.getWorld(ODS.unwrap((StringTag) getTag("world")));
        boolean publicBar = byteToBool(ODS.unwrap((ByteTag) getTag("public")));
        MapTag<StringTag> mapTag = (MapTag<StringTag>) getTag("data");
        Map<String, String> data = new HashMap<>();
        for (Map.Entry<String, StringTag> mp : mapTag.getValue().entrySet()) {
            data.put(mp.getKey(), mp.getValue().getValue());
        }
        String permission = null;
        if (hasTag("permission"))
            permission = ODS.unwrap((StringTag) getTag("permission"));
        boolean hasTimer = byteToBool(ODS.unwrap((ByteTag) getTag("hasTimer")));
        CountStyle countStyle = CountStyle.DOWN;
        if (hasTag("countStyle"))
            countStyle = ODS.unwrap((ByteTag) getTag("countStyle")) == 0 ? CountStyle.UP : CountStyle.DOWN;
        BossBarBuilder bbb = new BossBarBuilder(byteToBool(ODS.unwrap((ByteTag) getTag("tracked"))))
                .setId(id)
                .setMessage(message)
                .setColor(barColor)
                .setStyle(barStyle)
                .setTime(time)
                .setProgress(progress)
                .setWorld(w)
                .setPublicBar(publicBar)
                .setData(data)
                .setTracked(byteToBool(ODS.unwrap((ByteTag) getTag("tracked"))))
                .setPermission(permission)
                .setCountStyle(countStyle);
        if (hasTimer)
            return bbb.build();
        else
            return bbb.buildDead();
    }

    private boolean byteToBool(byte bit) {
        return bit == 1;
    }
}
