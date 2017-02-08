package org.alexside;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private static final String DATA_TEST = "Hello,everyone.My names is Alex";
    public static void main(String[] args) {
        String pi4b = piSpigot(2048).substring(2);
        System.out.println(pi4b);

        List<Boolean> bitMask = new ArrayList<>();
        List<Character> characters = new ArrayList<>();

        int curIndex = 0;
        int[] piDigits = toIntArray(pi4b);
        char[] data = DATA_EN.toCharArray();

        for (char c : data) {
            if (curIndex >= piDigits.length) break;
            List<Boolean> charBitMask = new ArrayList<>();
            List<Integer> piSeq = new ArrayList<>();

            int[] charInt = toIntArray(c);
            for (int i : charInt) {
                while(curIndex < piDigits.length) {
                    int piDigit = piDigits[curIndex++];
                    piSeq.add(piDigit);
                    if (i == piDigit) {
                        charBitMask.add(true);
                        break;
                    } else {
                        charBitMask.add(false);
                    }
                }
            }
            System.out.printf("char = %s, code = %s, bitMask = %s, bitMaskLen = %s, piSeq = %s\n",
                    c, Arrays.toString(charInt), toBitString(charBitMask), charBitMask.size(), toIntString(piSeq));
            bitMask.addAll(charBitMask);
            characters.add(c);
        }

//        for (int intChar : toIntArray(pi4b)) {
//            if (curIndex >= data.length) break;
//
//            List<Boolean> digitBitMask = new ArrayList<>();
//
//            int[] cInt = toIntArray(data[curIndex]);
//
//
//            for (char c : String.valueOf(intChar).toCharArray()) {
//
//            }
//            if (digit == data[curIndex++]) {
//                digitBitMask.add(true);
//                break;
//            } else digitBitMask.add(false);
//
//            System.out.printf("%s -> %s -> %s\n", (char)intChar, intChar, toBitString(digitBitMask));
//            bitMask.addAll(digitBitMask);
//        }

        System.out.println("-------------------------------------------------------");
        System.out.printf("pi:   %s\n", pi4b);
        System.out.printf("bits: %s\n", toBitString(bitMask));
        System.out.printf("bitMask size: %s\n", bitMask.size());
        String reduced = reduceNills(3, 4, toBitString(bitMask));
        System.out.printf("bits (reduced): %s\n", reduced);
        System.out.printf("bitMask size (reduced): %s\n", reduced.length());
        System.out.printf("bytes: %s\n", characters.size()*2);

//        DATA.chars().forEach(p -> {
//            System.out.printf("%s -> %s\n", (char)p, p);
//        });

    }

    public static String piSpigot(final int n) {
        // найденные цифры сразу же будем записывать в StringBuilder
        StringBuilder pi = new StringBuilder(n);
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
            pi.insert(1, '.');	// добавляем в строчку точку после 3
        }
        return pi.toString();
    }

    private static String toStr(boolean v) { return v ? "1" : "0"; }
    private static String toBitString(List<Boolean> list) {
        return list.stream().map(v -> toStr(v)).reduce("", String::concat);
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

    private static String archive(byte[] bytes) {
        return "";
    }

    private static byte[] extract(String digest) {
        return new byte[] {};
    }

    /**
     * Сокращение строки путем выпиливания нулей перед 1
     * @param min - порог нулей, которые не будут сокращаться
     * @param n - кол-во нулей для выпиливания
     * @param seq - строка с 0 и 1
     * @return
     */
    private static String reduceNills(int min, int n, String seq) {
        if (seq.length() < 10) return seq;
        StringBuilder sb = new StringBuilder();
        for (String s : seq.split("1")) {
            String r = s;
            if (!s.isEmpty()) {
                try {
                    if ((s.length() - n) > min) {
                        r = s.substring(0, n);
                    }
                } catch (Throwable e) {
                    String bad = "";
                }
            }
            sb.append(r);
            sb.append("1");
        }
        String result = sb.toString();
        return result.substring(0, result.lastIndexOf("1"));
    }

    private static String restoreNills(int min, int n, String seq) {
        if (seq.length() < 10) return seq;
        return "";
    }
}

