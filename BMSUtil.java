package com.kuna.sabuneditor_android.bms;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.util.Log;

public class BMSUtil {
	public static void Log(String title, String desc) {
		/* your customize part ! */
		Log.i(title, desc);
	}

	public static String GetHash(byte[] data) {
		MessageDigest md;
		String hash = null;
		try {
			md = MessageDigest.getInstance("MD5");
			hash = new BigInteger(1, md.digest( data )).toString(16);
		} catch (NoSuchAlgorithmException e) {
			Log.i("BMSUtil", "Hashing Error!");
			e.printStackTrace();
		}
		return hash;
	}
	
	public static boolean IsInteger(String str) {
		return Pattern.compile("-?[0-9]+").matcher(str).matches();
	}
	
	public static int ExtHexToInt(String hex) {
		String sample = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int r = 0;
		for (int i=0; i<hex.length(); i++) {
			r *= 36;
			for (int j=0; j<sample.length(); j++) {
				if (hex.substring(i, i+1).compareTo( sample.substring(j, j+1) )==0) {
					r += j;
					continue;
				}
			}
		}
		
		return r;
	}
	
	public static String IntToExtHex(int val) {
		String sample = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		return new Character(sample.charAt((int)(val/36))).toString() + new Character(sample.charAt(val%36)).toString();
	}
	
	public static int HexToInt(String hex) {
		return (int) Long.parseLong(hex, 16);
	}
	
	public static String IntTo2Hex(int val) {
		return String.format("%02X", val);
	}
	
	public static ArrayList<BMSKeyData> cloneKeyArray(List<BMSKeyData> bmsdata) {
		ArrayList<BMSKeyData> a = new ArrayList<BMSKeyData>();
		for (BMSKeyData bkd: bmsdata) {
			a.add(cloneKeyData(bkd));
		}
		return a;
	}
	
	public static BMSKeyData cloneKeyData(BMSKeyData bkd) {
		if (bkd.key == 2) {
			Log.e("UNEXPRECTED", "UNEXPECTED");
		}
		BMSKeyData b = new BMSKeyData();
		b.attr = bkd.attr;
		b.beat = bkd.beat;
		b.key = bkd.key;
		b.layernum = bkd.layernum;
		b.numerator = bkd.numerator;
		b.time = bkd.time;
		b.value = bkd.value;
		return b;
	}
	
	public static List<BMSKeyData> ExtractChannel(List<BMSKeyData> data, int channel) {
		ArrayList<BMSKeyData> r = new ArrayList<BMSKeyData>();
		for (BMSKeyData b: data) {
			if (b.getChannel() == channel) {
				r.add(b);
			}
		}
		
		return r;
	}
	
	public static List<BMSKeyData> ExtractLayer(List<BMSKeyData> data, int layer) {
		ArrayList<BMSKeyData> r = new ArrayList<BMSKeyData>();
		for (BMSKeyData b: data) {
			if (b.getLayerNum() == layer) {
				r.add(b);
			}
		}
		
		return r;
	}
}
