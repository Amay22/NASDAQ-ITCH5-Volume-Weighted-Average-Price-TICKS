import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
public class VWAP {
    public static class VWAPData {
        double price;
        int volume;
        public VWAPData(double price, int volume) {
            this.price = price;
            this.volume = volume;
        }
        @Override
        public String toString() {
            return (this.price + " " + this.volume);
        }
    }
    static HashMap<String, ArrayList<VWAPData>> C = new HashMap<>();
    public static void main(String args[]) throws IOException, InterruptedException {
        HashMap<Long, String> A = new HashMap<>();
        InputStream input = new FileInputStream(new File("06022014.NASDAQ_ITCH50"));
        while (input.read() != -1) {
            int payLength = input.read();
            byte[] payBytes = new byte[payLength];
            input.read(payBytes);
            String str = new String(payBytes);
            switch (str.charAt(0)) {
                case 'A':
                    if ((char) payBytes[19] == 'B') {
                        A.put((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong()), 
                            "" + ((char) payBytes[24]) + "" + ((char) payBytes[25]) 
                            + "" + ((char) payBytes[26]) + "" + ((char) payBytes[27]) 
                            + "" + ((char) payBytes[28]) + "" + ((char) payBytes[29])
                            + "" + ((char) payBytes[30]) + "" + ((char) payBytes[31])
                            + "" + ((char) payBytes[32]));
                    }
                    break;
                case 'C':
                    if ((char) payBytes[31] == 'Y' && A.get((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong())) != null) {
                        if (C.get(A.get((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong()))) == null) {
                            ArrayList<VWAPData> temp = new ArrayList<>();
                            temp.add(new VWAPData(((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 32, 36)).getInt()) / 10000),
                                    (ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 19, 23)).getInt())));
                            C.put(A.get((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong())), temp);
                        } else {
                            ArrayList<VWAPData> temp = C.get(A.get((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong())));
                            temp.add(new VWAPData(
                                    (((double) (ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 32, 36)).getInt())) / 10000),
                                    (ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 19, 23)).getInt())));
                            C.put(A.get((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong())), temp);
                        }
                        A.remove((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong()));
                    }
                    break;
                case 'X':
                case 'D':
                case 'U':
                    A.remove((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong()));
                    break;
            }
        }
        String output = "<html><body>";
        double cummulative_price, vol_avg;
        long cummulative_volume;
        for (Map.Entry<String, ArrayList<VWAPData>> entrySet : C.entrySet()) {
            String key = entrySet.getKey();
            ArrayList<VWAPData> value = entrySet.getValue();
            cummulative_price = 0;
            vol_avg = 0;
            cummulative_volume = 0;
            int i = 0;
            output += "<BR><BOLD>" + key + "</BOLD><BR><table border=\"1\"><tr><td>Tick</td><td>Price</td><td>Volume</td><td></td><td>&#8721(volume)</td>"
                    + "<td>&#8721(volume x Price)</td><td></td><td>Price</td><td>Volume Weighted Average Price</td></tr>";
            for (Iterator<VWAPData> iterator = value.iterator(); iterator.hasNext();) {
                i++;
                VWAPData next = iterator.next();
                cummulative_price += next.volume * next.price;
                cummulative_volume += next.volume;
                vol_avg = cummulative_price / cummulative_volume;
                output += "<tr><td>" + i + "</td><td>" + next.price + "</td><td>" + next.volume + "</td><td>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</td>"
                        + "<td>" + cummulative_volume + "</td><td>" + cummulative_price + "</td><td>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</td><td>"
                        + next.price + "</td><td>" + vol_avg + "</td></tr>";
            }
            output += "</table><BR>";
        }
        output += "<body><html>";
        FileWriter fw = new FileWriter("test.htm");
        try (BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(output);
        }
        Desktop.getDesktop().open(new File("test.htm"));
        System.out.println("Done");
    }
}
