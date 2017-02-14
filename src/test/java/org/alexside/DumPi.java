package org.alexside;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by abalyshev on 08.02.17.
 */
public class DumPi {
    private static final String DATA_RU =
            "Далее, чтобы найти одну цифру числа Пи, необходимо пройтись по всем ячейкам массива с конца к началу и выполнить несложные действия. На примере таблицы, рассмотрим всё по порядку. Допустим, мы хотим найти 3 цифры числа Пи. Для этого нам необходимо зарезервировать 3 * 10 / 3 = 10 ячеек целого типа. Заполняем их все числом 2. Теперь приступим к поиску первой цифры…\n" +
                    "\n" +
                    "Начинаем с конца массива. Берём последний элемент (под номером 9, если начинать счёт с 0. Этот же номер будем называть числителем, а тот, что под ним в таблицу – знаменателем) — он равен 2. Умножаем его на 10 (2 * 10 = 20). К получившемуся числу 20 прибавляем число из ячейки «Перенос» – число, которое переносится из более правой операции. Разумеется, правее мы ничего не считали, поэтому это число равно 0. Результат записываем в «сумму». В «остаток» записываем остаток от деления суммы на знаменатель: 20 mod 19 = 1. А сейчас считаем «перенос» для следующего шага. Он будет равен результату деления суммы на знаменатель, умноженному на числитель: (20 / 19) * 9 = 9. И записываем полученное число в ячейку с «переносом», стоящую левее от текущего столбца. Проделываем те же действия с каждым элементом массива (умножить на 10, посчитать сумму, остаток и перенос на следующий шаг), пока не дойдём до элемента с номером 0. Здесь действия немного отличаются. Итак, посмотрим, что у нас в таблице. Под нулём – элемент массива, равный 2, и перенос из предыдущего шага, равный 10. Как и в предыдущих шагах, умножаем элемент массива на 10 и прибавляем к нему перенос. В сумме получили 30. А сейчас делим сумму не на знаменатель, а на 10 (!). В итоге получаем 30 / 10 = 3 + 0 (где 0 – остаток). Полученное число 3 и будет той заветной первой цифрой числа Пи. А остаток 0 записываем в отведённую ему ячейку. Для чего были остатки? Их нужно использовать в следующей итерации – чтобы найти следующую цифру числа Пи. Поэтому разумно сохранять остатки в наш изначальный массив размером 10 * n / 3. Таким образом, видно, что возможности алгоритма упираются именно в размер этого массива. Поэтому число найденных цифр ограничивается доступной памятью компьютера либо языком, на котором вы реализуете алгоритм (конечно, языковые ограничения можно обойти).\n" +
                    "\n" +
                    "Но это не всё. Есть один нюанс. В конце каждой итерации может возникать ситуация переполнения. Т.е. в нулевом столбце в «сумме» мы получим число, большее, чем 100 (эта ситуация возникает довольно редко). Тогда следующей цифрой Пи получается 10. Странно, да? Ведь цифр в десятичной системе счисления всего от 0 до 9. В этом случае вместо 10 нужно писать 0, а предыдущую цифру увеличивать на 1 (и стоящую перед последней, если последняя равна 9, и т.д.). Таким образом, появление одной десятки, может изменить одну и больше найденных ранее цифр. Как отслеживать такие ситуации? Необходимо найденную новую цифру первоначально считать недействительной. Для этого достаточно завести одну переменную, которая будет считать количество недействительных цифр. Нашли одну цифру – увеличили количество недействительных цифр на 1. Если следующая найденная цифра не равна ни 9, ни 10, то начинаем считать найденные ранее цифры (и помеченные как недействительные) действительными, т.е. сбрасываем количество недействительных цифр в 0, а найденную новую цифру начинаем считать недействительной (т.е. можно сразу сбрасывать в 1). Если следующая найденная цифра равна 9, то увеличиваем количество недействительных цифр на 1. Если же следующая цифра 10, то увеличиваем все недействительные цифры на 1, вместо 10 записываем 0 и этот 0 считаем недействительным. Если эти ситуации не отслеживать, то будут появляться единичные неверные цифры (или больше).\n" +
                    "\n" +
                    "Вот и весь алгоритм, который авторы сравнили с краном. Ниже привожу код метода, реализованного на Java, который возвращает строку с числом Пи.";
    private static final String DATA_EN = "James Gosling, Mike Sheridan, and Patrick Naughton initiated the Java language project in June 1991.[22] Java was originally designed for interactive television, but it was too advanced for the digital cable television industry at the time.[23] The language was initially called Oak after an oak tree that stood outside Gosling's office. Later the project went by the name Green and was finally renamed Java, from Java coffee.[24] Gosling designed Java with a C/C++-style syntax that system and application programmers would find familiar.[25]\n" +
            "\n" +
            "Sun Microsystems released the first public implementation as Java 1.0 in 1995.[26] It promised \"Write Once, Run Anywhere\" (WORA), providing no-cost run-times on popular platforms. Fairly secure and featuring configurable security, it allowed network- and file-access restrictions. Major web browsers soon incorporated the ability to run Java applets within web pages, and Java quickly became popular, while mostly outside of browsers, that wasn't the original plan. In January 2016, Oracle announced that Java runtime environments based on JDK 9 will discontinue the browser plugin.[27] The Java 1.0 compiler was re-written in Java by Arthur van Hoff to comply strictly with the Java 1.0 language specification.[28] With the advent of Java 2 (released initially as J2SE 1.2 in December 1998 – 1999), new versions had multiple configurations built for different types of platforms. J2EE included technologies and APIs for enterprise applications typically run in server environments, while J2ME featured APIs optimized for mobile applications. The desktop version was renamed J2SE. In 2006, for marketing purposes, Sun renamed new J2 versions as Java EE, Java ME, and Java SE, respectively.\n" +
            "\n" +
            "In 1997, Sun Microsystems approached the ISO/IEC JTC 1 standards body and later the Ecma International to formalize Java, but it soon withdrew from the process.[29][30][31] Java remains a de facto standard, controlled through the Java Community Process.[32] At one time, Sun made most of its Java implementations available without charge, despite their proprietary software status. Sun generated revenue from Java through the selling of licenses for specialized products such as the Java Enterprise System.\n" +
            "\n" +
            "On November 13, 2006, Sun released much of its Java virtual machine (JVM) as free and open-source software, (FOSS), under the terms of the GNU General Public License (GPL). On May 8, 2007, Sun finished the process, making all of its JVM's core code available under free software/open-source distribution terms, aside from a small portion of code to which Sun did not hold the copyright.[33]\n" +
            "\n" +
            "Sun's vice-president Rich Green said that Sun's ideal role with regard to Java was as an \"evangelist\".[34] Following Oracle Corporation's acquisition of Sun Microsystems in 2009–10, Oracle has described itself as the \"steward of Java technology with a relentless commitment to fostering a community of participation and transparency\".[35] This did not prevent Oracle from filing a lawsuit against Google shortly after that for using Java inside the Android SDK (see Google section below). Java software runs on everything from laptops to data centers, game consoles to scientific supercomputers.[36] On April 2, 2010, James Gosling resigned from Oracle.[37]";
    private static final String DATA_TEST = "Hello everyone. My name is Alex";
    public static void main(String[] args) {
        byte[] digest = piBytes(512);
        byte[] source = DATA_TEST.getBytes();
        byte[] bitMask = compressData(digest, source);

        String digestBin = toBitString(digest);
        System.out.printf("digest (binary) : %s\n", digestBin);
        System.out.printf("digest (hex)    : %s\n", Arrays.toString(digest));
        System.out.printf("digest length:  %s\n", digest.length);

        String sourceBin = toBitString(source);
        System.out.printf("source (binary) : %s\n", sourceBin);
        System.out.printf("source (hex)    : %s\n", Arrays.toString(source));
        System.out.printf("source  length: %s\n", source.length);

        String bitMaskBin = toBitString(bitMask);
        System.out.printf("bitmask (binary): %s\n", bitMaskBin);
        System.out.printf("bitmask (hex)   : %s\n", Arrays.toString(bitMask));
        System.out.printf("bitmask length: %s\n", bitMask.length);

        List<String> reduced = reduceNills2(toBitString(bitMask));
        System.out.printf("bitmask reduced (binary): %s\n", Arrays.toString(reduced.toArray(new String[reduced.size()])));
        String reducedStr = reduced.stream().reduce("", (s1, s2) -> s1+s2);
        String srcStr = toBitString(bitMask);
        System.out.printf("bitmask reduced length: %s, byte length: %s, bitMask byte length: %s, src byte length: %s\n",
                reducedStr.length()/8, reducedStr.getBytes().length, srcStr.getBytes().length, source.length);

        String sourceBinTest = "01001000111111011101000000110011100101100111";
        String digestBinTest = "10111000111100100010000011001100100101101000";
        String bitMaskBinTest = "01100100110";
        System.out.printf("Mask validation: %s\n", validMask(sourceBinTest, digestBinTest, bitMaskBinTest) ? "SUCCESS" : "FAILURE");
        System.out.println("// --------------------------------------------------------------------------------------");

        reduceNills(bitMask);

//        byte b = -111;
//        byte b2 = -111;
//        byte[] split = splitByBits(b,1);
//        System.out.println(isMask(split[0], b2, 0));
//        System.out.println(isMask(split[1], b2, 1));
//        System.out.println(Integer.toBinaryString(b & 0xff));
//        System.out.println(Integer.toBinaryString(
//                concatByBits(split[0], split[1]) & 0xff));
//        System.out.println(Integer.toBinaryString((byte)((b >> 4) & (byte)0x0F) & 0xff));
//        System.out.println(Integer.toBinaryString(split[0] & 0xff));
//        System.out.println(Integer.toBinaryString((b & 0x0F) & 0xff));
//        System.out.println(Integer.toBinaryString(split[1] & 0xff));

//        System.out.println(Arrays.toString("0021371097147847832".split("(?<=\\G.{3})")));
//        System.out.println(pi4b);
//        System.out.println("-------------------------------------------------------");
//        System.out.printf("pi:   %s\n", pi4b);
//        String bitMaskStr = toBitString(bitMask);
//        System.out.printf("bits: %s\n", bitMaskStr);
//        System.out.printf("units: %s, zeros: %s\n", bitMaskStr.replaceAll("0", "").length(), bitMaskStr.replaceAll("1", "").length());
//        System.out.printf("bitMask size: %s\n", bitMask.size());
//        List<String> reduced2 = reduceNills2(bitMaskStr);
//        System.out.printf("bits (reduced): %s\n", Arrays.toString(reduced2.toArray()));
//        int bitMaskSize = getReducedSize(reduced2);
//        System.out.printf("bitMask size (reduced): %s\n", bitMaskSize);
//        String restored = restoreNills(reduced2);
//        System.out.printf("source   bits: %s\n", bitMaskStr);
//        System.out.printf("restored bits: %s\n", restored);
//        System.out.printf("Has restored: %s, srcSize: %s rstSize: %s\n",
//                Objects.equals(bitMaskStr, restored), characters.size()*2, bitMaskSize/8);

//        byte b1 = -124;
//        System.out.printf("%s -> %s\n", b1, Integer.toBinaryString(b1 & 0xff));
        //compressData(pi4b);
    }

    private static boolean validMask(String src, String digest, String mask) {
        for (int ndx=0; ndx < src.length(); ndx+=4) {
            boolean valid = true;
            char maskBit = mask.charAt(ndx/4);
            if (maskBit == '0' &&  Objects.equals(src.substring(ndx, ndx+4), digest.substring(ndx, ndx+4)))
                valid = false;
            else if (maskBit == '1' && !Objects.equals(src.substring(ndx, ndx+4), digest.substring(ndx, ndx+4)))
                valid = false;

            System.out.printf("ndx: ->  %s, source -> %s, digest -> %s, mask -> %s, valid -> %s\n",
                    ndx/4,
                    src.substring(ndx, ndx+4),
                    digest.substring(ndx, ndx+4),
                    maskBit,
                    valid
            );
            if (!valid) return false;
        }
        return true;
    }

    private static void printBytes(byte[] bytes) {
        IntStream.range(0, bytes.length).forEach(ndx -> {
            System.out.printf("ndx: %s, byte: %s, bits: %s\n",
                    ndx, bytes[ndx], Integer.toBinaryString(bytes[ndx] & 0xff));
        });
    }

    private static byte[] compressData(byte[] digest, byte[] source) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); // битовая маска в формате byte[]
        int digestNdx = 0;// текущий байт дайджеста
        int imposedBits = 0; // текущая позиция наложенния битов для source[ndx](пока 0 и 1 т.е. накладываем по 4 бита)
        boolean imposed; // флаг успешного наложения маски

        for (int ndx=0; ndx < source.length; ++ndx) {
            imposed = false;
            StringBuilder bitMask = new StringBuilder();
            byte[] bitChunks = new byte[8];
            int chunkCnt = 0;



//            while(!imposed) {
//                digestNdx = digestNdx >= digest.length ? 0 : digestNdx;
//                byte[] dBits = splitByBits(digest[digestNdx++]);
//                for (int i=0; i < dBits.length; ++i) {
//                    bitChunks[chunkCnt++] = dBits[i];
//                    if (isMask(dBits[i], source[ndx], imposedBits)) {
//                        imposedBits++;
//                        bitMask.append("1");
//                    } else {
//                        bitMask.append("0");
//                    }
//                    if (bitMask.toString().length() >= 8) {
//                        // считали 8 бит - конкатенируем байт, добавляем в буфер
//                        String s = bitMask.toString().substring(0, 8);
//                        byte b = (byte)Integer.parseInt(s, 2);
//                        System.out.printf("ndx: %s, dNdx: %s, src -> %s, src bits -> %s, src chunks -> %s, bit mask -> %s, byte chunks -> %s, imposedBits -> %s\n",
//                                ndx, digestNdx,
//                                source[ndx],
//                                Integer.toBinaryString(source[ndx] & 0xff),
//                                Arrays.toString(splitByBits(source[ndx])),
//                                s,
//                                Arrays.toString(bitChunks),
//                                imposedBits);
//                        baos.write(b);
//                        chunkCnt = 0;
//                        bitMask.setLength(0);
//                    }
//                    if (imposedBits >= 2) {
//                        imposed = true;
//                        imposedBits = 0;
//                    }
//                }
//            }
        }
        return baos.toByteArray();
    }

    private static boolean isMask(byte digest, byte source, int bit) {
        if (bit % 2 == 0) {
            return digest == (byte)((source >> 4) & (byte)0x0F);
        }
        return digest == (byte)(source & 0x0f);
    }

    private static byte[] splitByBits(byte b, int bits) {
        byte low = (byte) ((b >> 4) & (byte)0x0F);
        byte high = (byte) (b & 0x0F);
        return new byte[] { low, high };
    }

    private static byte[] splitByBits(byte b) {
        return splitByBits(b, 1);
    }

    private static byte concatByBits(byte low, byte high) {
        return (byte) ((low << 4) | high);
    }

    public static String toBinaryString(byte b) {
        StringBuilder buf = new StringBuilder();
        IntStream.range(1, 9).forEach(bit -> buf.append(isSet(b, bit) ? "1" : "0"));
        return buf.toString();
    }

    public static boolean isSet(byte value, int bit) {
        int b = (int)value & 0xff;
        b >>= bit;
        b &= 0x01;
        if( b != 0 ) {
            return true;
        }
        return false;
    }

    public static byte setBit(byte value, int bit) {
        int b = (int)value;
        b |= (1 << bit);
        return (byte)(b & 0xff);
    }

    public static byte clearBit(byte value, int bit) {
        int b = (int)value;
        b &= ~(1 << bit);
        return (byte)(b & 0xff);
    }

    public enum BitMask {
        BIT1(1), BIT2(2), BIT4(4), BIT8(8);
        private int bits;
        BitMask(int bits) { this.bits = bits; }
        public int getBits() { return bits; }
    }

//    private static void compressData(String pi4b) {
//        List<Boolean> bitMask = new ArrayList<>();
//        List<Character> characters = new ArrayList<>();
//
//        int curIndex = 0;
//        int[] piDigits = toIntArray(pi4b);
//        char[] data = DATA_EN.toCharArray();
//
//        for (int ndx = 0; ndx < data.length; ++ndx) {
//            char c = data[ndx];
//            List<Boolean> charBitMask = new ArrayList<>();
//            List<Integer> piSeq = new ArrayList<>();
//
//            int[] charInt = toIntArray(c);
//            for (int i : charInt) {
//                while(curIndex < piDigits.length) {
//                    int piDigit = piDigits[curIndex++];
//                    if (curIndex >= piDigits.length) curIndex = 0;
//                    piSeq.add(piDigit);
//                    if (i == piDigit) {
//                        charBitMask.add(true);
//                        break;
//                    } else {
//                        charBitMask.add(false);
//                    }
//                }
//            }
//            System.out.printf("%s: char = %s, code = %s, bitMask = %s, bitMaskLen = %s, piSeq = %s\n",
//                    ndx, c, Arrays.toString(charInt), toBitString(charBitMask), charBitMask.size(), toIntString(piSeq));
//            bitMask.addAll(charBitMask);
//            characters.add(c);
//        }
//
//        System.out.println(pi4b);
//        System.out.println("-------------------------------------------------------");
//        System.out.printf("pi:   %s\n", pi4b);
//        String bitMaskStr = toBitString(bitMask);
//        System.out.printf("bits: %s\n", bitMaskStr);
//        System.out.printf("units: %s, zeros: %s\n", bitMaskStr.replaceAll("0", "").length(), bitMaskStr.replaceAll("1", "").length());
//        System.out.printf("bitMask size: %s\n", bitMask.size());
//        List<String> reduced2 = reduceNills2(bitMaskStr);
//        System.out.printf("bits (reduced): %s\n", Arrays.toString(reduced2.toArray()));
//        int bitMaskSize = getReducedSize(reduced2);
//        System.out.printf("bitMask size (reduced): %s\n", bitMaskSize);
//        String restored = restoreNills(reduced2);
//        System.out.printf("source   bits: %s\n", bitMaskStr);
//        System.out.printf("restored bits: %s\n", restored);
//        System.out.printf("Has restored: %s, srcSize: %s rstSize: %s\n",
//                Objects.equals(bitMaskStr, restored), characters.size()*2, bitMaskSize/8);
//    }

//    public static String piSpigot(final int n) {
//        // найденные цифры сразу же будем записывать в StringBuilder
//        StringBuilder pi = new StringBuilder(n);
//        int boxes = n * 10 / 3;	// размер массива
//        int reminders[] = new int[boxes];
//        // инициализируем масив двойками
//        for (int i = 0; i < boxes; i++) {
//            reminders[i] = 2;
//        }
//        int heldDigits = 0;    // счётчик временно недействительных цифр
//        for (int i = 0; i < n; i++) {
//            int carriedOver = 0;    // перенос на следующий шаг
//            int sum = 0;
//            for (int j = boxes - 1; j >= 0; j--) {
//                reminders[j] *= 10;
//                sum = reminders[j] + carriedOver;
//                int quotient = sum / (j * 2 + 1);   // результат деления суммы на знаменатель
//                reminders[j] = sum % (j * 2 + 1);   // остаток от деления суммы на знаменатель
//                carriedOver = quotient * j;   // j - числитель
//            }
//            reminders[0] = sum % 10;
//            int q = sum / 10;	// новая цифра числа Пи
//            // регулировка недействительных цифр
//            if (q == 9) {
//                heldDigits++;
//            } else if (q == 10) {
//                q = 0;
//                for (int k = 1; k <= heldDigits; k++) {
//                    int replaced = Integer.parseInt(pi.substring(i - k, i - k + 1));
//                    if (replaced == 9) {
//                        replaced = 0;
//                    } else {
//                        replaced++;
//                    }
//                    pi.deleteCharAt(i - k);
//                    pi.insert(i - k, replaced);
//                }
//                heldDigits = 1;
//            } else {
//                heldDigits = 1;
//            }
//            pi.append(q);	// сохраняем найденную цифру
//        }
//        if (pi.length() >= 2) {
//            //pi.insert(1, '.');	// добавляем в строчку точку после 3
//        }
//        return pi.toString();
//    }

    public static String[] splitByLen(String s, int len) {
        String[] split = new String[len+1];
        for (int i=0, ndx=0; i < s.length(); i += len, ++ndx) {
            split[ndx] = s.substring(ndx, Math.max(ndx, s.length()));
        }
        return split;
    }

    public static byte[] splitOnBytes(String s, int len) {
        byte[] split = new byte[s.length() / len + 1];
        for (int i=0, ndx=0; i < s.length(); i += len, ++ndx) {
            String strNum = s.substring(i, Math.min(i + len, s.length()));
            byte byteNum = (byte) Integer.parseInt(strNum);
            split[ndx] = byteNum;
//            System.out.printf("ndx: %s, num: %s, byte: %s, bits: %s\n",
//                    i, strNum, byteNum, Integer.toBinaryString(byteNum & 0xff));
        }
        return split;
    }

    public static byte[] piBytes(final int n) {
        // найденные цифры сразу же будем записывать в StringBuilder
        StringBuilder pi = new StringBuilder();
        int boxes = n * 10 / 3;	// размер массива
        int reminders[] = new int[boxes];
        // инициализируем масив двойками
        for (int i = 0; i < boxes; i++) {
            reminders[i] = 2;
        }
        int heldDigits = 0;    // счётчик временно недействительных цифр
        for (int i = 0; i < n; i++) {
            int carriedOver = 0;    // перенос на следующий шаг
            int sum = 0;
            for (int j = boxes - 1; j >= 0; j--) {
                reminders[j] *= 10;
                sum = reminders[j] + carriedOver;
                int quotient = sum / (j * 2 + 1);   // результат деления суммы на знаменатель
                reminders[j] = sum % (j * 2 + 1);   // остаток от деления суммы на знаменатель
                carriedOver = quotient * j;   // j - числитель
            }
            reminders[0] = sum % 10;
            int q = sum / 10;	// новая цифра числа Пи
            // регулировка недействительных цифр
            if (q == 9) {
                heldDigits++;
            } else if (q == 10) {
                q = 0;
                for (int k = 1; k <= heldDigits; k++) {
                    int replaced = Integer.parseInt(pi.substring(i - k, i - k + 1));
                    if (replaced == 9) {
                        replaced = 0;
                    } else {
                        replaced++;
                    }
                    pi.deleteCharAt(i - k);
                    pi.insert(i - k, replaced);
                }
                heldDigits = 1;
            } else {
                heldDigits = 1;
            }
            pi.append(q);	// сохраняем найденную цифру
        }
        if (pi.length() >= 2) {
            //pi.insert(1, '.');	// добавляем в строчку точку после 3
        }
        return splitOnBytes(pi.toString(), 3);
    }

    private static String toStr(boolean v) { return v ? "1" : "0"; }
    private static String toBitString(List<Boolean> list) {
        return list.stream().map(v -> toStr(v)).reduce("", String::concat);
    }
    private static String toHexString(byte[] bytes) {
        return IntStream
                .range(0, bytes.length)
                .mapToObj(ndx -> bytes[ndx])
                .map(String::valueOf)
                .reduce("", (s1, s2) -> s1 + s2);
    }
    private static String toBitString(BitSet bitSet, int length) {
        return IntStream
                .range(0, length)
                .mapToObj(bitSet::get)
                .map(bit -> bit ? "1" : "0")
                .reduce("", (s1, s2) -> s1 + s2);
    }
    private static String toBitString(byte[] bytes) {
        return IntStream
                .range(0, bytes.length)
                .mapToObj(ndx -> bytes[ndx])
                .map(b -> toBinaryString(b))
                .reduce("", (s1, s2) -> s1 + s2);
    }
    private static String toIntString(List<Integer> list) {
        return list.stream().map(String::valueOf).reduce("", String::concat);
    }
    private static int[] toIntArray(String s) {
        return s.chars()
                .mapToObj(c->(char)c)
                .map(String::valueOf)
                .map(Integer::parseInt)
                .mapToInt(i -> i)
                .toArray();
    }
    private static int[] toIntArray(char c) {
        return toIntArray(String.valueOf((int)c));
    }

    private static class BitBuffer {
        private boolean[] bits;
        private int ndx;
        private BitBuffer() {
            this.bits = new boolean[8];
        }
        public void set(boolean bit) {
            if (ndx < bits.length) {
                bits[ndx++] = bit;
            }
        }
        public boolean isSet(int ndx) {
            if (ndx < bits.length) return false;
            return bits[ndx];
        }
        public boolean isFull() {
            return ndx >= (bits.length-1);
        }
        public void clear() {
            IntStream.range(0, bits.length).forEach(ndx -> bits[ndx] = false);
            ndx = 0;
        }
        public byte toByte() {
            StringBuilder buf = new StringBuilder();
            IntStream.range(0, bits.length).forEach(ndx -> {
                buf.append(bits[ndx] ? "1" : "0");
            });
            return (byte)Integer.parseInt(buf.toString(), 2);
        }
        public static BitBuffer create() {
            return new BitBuffer();
        }
    }

    private static byte[] reduceNills(byte[] bitMask) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        int nills = 0;
//        BitBuffer bitBuffer = BitBuffer.create();
//        for (int ndx=0; ndx < bitMask.length; ++ndx) {
//            for (int n=1; n < 9; ++n) {
//                boolean bit = isSet(bitMask[ndx], n);
//                bitBuffer.set(bit);
//                if (!bit) {
//                    nills++;
//                } else {
//
//                }
//            }
//        }
        BitSet reducedBitSet = new BitSet();
        BitSet bitSet = BitSet.valueOf(bitMask);
        System.out.println("Cardinality: " + bitSet.cardinality());
        System.out.println("BitMask: " + toBitString(bitMask));
        System.out.println("BitMask size: " + bitMask.length);
        System.out.println("BitSet:  " + toBitString(bitSet.toByteArray()));
        System.out.println("BitSet: size: " + bitSet.toByteArray().length);
        int curSet=0, prevSet=0;
        while ((curSet = bitSet.nextSetBit(curSet)) != -1) {
            System.out.printf("prevSet: %s, curSet: %s, bits(%s, %s): %s\n",
                    prevSet, curSet, prevSet, curSet, toBitString(bitSet.get(prevSet, curSet), curSet-prevSet));
            prevSet = curSet;
            curSet += 1;
        }
        return reducedBitSet.toByteArray();
    }

    private static int nillsBefore(byte b) {
        int nills = 0;
        for (int bit=1; bit < 9; ++bit) {
            if (isSet(b, bit)) return nills;
            ++nills;
        }
        return nills;
    }

    private static List<String> reduceNills2(String seq) {
        List<String> result = new ArrayList<>();
        if (seq.length() < 10) {
            result.add(seq);
            return result;
        }
        String[] split = splitSequence(seq);
        if (split.length == 0) {

        } else {
            IntStream.range(0, split.length).forEach(ndx -> {
                String bin = split[ndx].length() > 0 ? Integer.toBinaryString(split[ndx].length()) : "";
                result.add(bin + (ndx == split.length-1 ? "" : "1"));
            });
        }
        return result;
    }

    private static String[] splitSequence(String seq) {
        List<String> result = new ArrayList<>();
        String buffer = "";
        try {
            for (int ndx=0; ndx < seq.length(); ++ndx) {
                if (seq.charAt(ndx) == '1') {
                    result.add(buffer);
                    buffer = "";
                } else if (seq.charAt(ndx) == '0') {
                    buffer += '0';
                }
            }
        } finally {
            result.add(buffer);
        }
        return result.toArray(new String[result.size()]);
    }

    private static int getReducedSize(List<String> reduced) {
        return reduced.stream().map(String::length).reduce((x, y) -> x + y).orElse(-1);
    }

    private static String restoreNills(List<String> seq) {
        StringBuilder sb = new StringBuilder();
        IntStream.range(0, seq.size()).forEach(ndx -> {
            int endNdx = ndx < seq.size() - 1 ? seq.get(ndx).lastIndexOf("1") : seq.get(ndx).length();
            String bin = seq.get(ndx).substring(0, endNdx);
            if (!bin.isEmpty()) {
                final String[] bits = {""};
                IntStream.range(0, Integer.parseInt(bin, 2)).forEach(ndx2 -> bits[0] += "0");
                sb.append(bits[0]);
                //System.out.printf("%s -> %s\n", bin, bits[0]);
            }
            if (ndx < seq.size() - 1)
                sb.append("1");
        });
        return sb.toString();
    }
}

