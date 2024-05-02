package com.emojis;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws IOException {
        File folder = new File("C:/Users/julli_000/Downloads/emojicon-master/emojicon-master/lib/res/drawable-nodpi");
        File[] listOfFiles = folder.listFiles();

        for (File f : folder.listFiles()) {
            if (f.isFile()) {
                if(f.getName().startsWith("emoji")) {
                    String emojiId = f.getName().replace("emoji_", "").replace("_","-");
                    System.out.println("File " + f.getName());
                    System.out.println("id " + emojiId);
                    if(!emojiId.equals("0023.png") && !emojiId.equals("0030.png")&& !emojiId.equals("0031.png")&& !emojiId.equals("0032.png")&& !emojiId.equals("0033.png") && !emojiId.equals("0034.png")
                            && !emojiId.equals("0035.png")&& !emojiId.equals("0036.png")&& !emojiId.equals("0037.png")&& !emojiId.equals("0038.png")&& !emojiId.equals("0039.png")
                            && !emojiId.equals("1f508.png") && !emojiId.equals("1f68b.png") && !emojiId.equals("2139.png")&& !emojiId.equals("24c2.png")&& !emojiId.equals("27bf.png")&& !emojiId.equals("3297.png")&& !emojiId.equals("3299.png"))
                        saveUrl("C:/Users/julli_000/Downloads/emojicon-master/emojicon-master/lib/res/drawable-nodpi/newemoji/emoji_" + emojiId.replace("-","_"), "http://apps.timwhitlock.info/static/images/emoji/emoji-android/" + emojiId);
                }
            }
        }
    }

    public static void saveUrl(final String filename, final String urlString)
            throws MalformedURLException, IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            in = new BufferedInputStream(new URL(urlString).openStream());
            fout = new FileOutputStream(filename);

            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }
}
