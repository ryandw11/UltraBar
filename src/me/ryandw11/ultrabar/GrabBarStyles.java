package me.ryandw11.ultrabar;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

public class GrabBarStyles {
	public static BarColor barColor(String color){
		BarColor bar;
		switch(color.toLowerCase()){
		case "blue":
			bar = BarColor.BLUE;
			break;
		case "green":
			bar = BarColor.GREEN;
			break;
		case "pink":
			bar = BarColor.PINK;
			break;
		case "purple":
			bar = BarColor.PURPLE;
			break;
		case "red":
			bar = BarColor.RED;
			break;
		case "white":
			bar = BarColor.WHITE;
			break;
		case "yellow":
			bar = BarColor.YELLOW;
			break;
		default:
				bar = BarColor.PURPLE;
				break;
		}
		return bar;
	}
	public static BarStyle barStyle(String sty){
		BarStyle st;
		switch(sty){
		case "6":
			st = BarStyle.SEGMENTED_6;
			break;
		case "10":
			st = BarStyle.SEGMENTED_10;
			break;
		case "12":
			st = BarStyle.SEGMENTED_12;
			break;
		case "20":
			st = BarStyle.SEGMENTED_20;
			break;
		case "":
			st = BarStyle.SOLID;
			break;
		default:
			st = BarStyle.SOLID;
			break;
		}
		return st;
	}
}
