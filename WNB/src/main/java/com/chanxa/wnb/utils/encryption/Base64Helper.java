package com.chanxa.wnb.utils.encryption;

import android.util.Base64;

public class Base64Helper {

	public static String encode(byte[] byteArray) {
		// sun.misc.BASE64Encoder base64Encoder = new sun.misc.BASE64Encoder();
		return Base64.encodeToString(byteArray, Base64.DEFAULT);
	}

	public static byte[] decode(String base64EncodedString) {
        //sun.misc.BASE64Decoder base64Decoder = new sun.misc.BASE64Decoder();
        try {
           // return base64Decoder.decodeBuffer(base64EncodedString);
            return  Base64.decode(base64EncodedString,Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }
}
