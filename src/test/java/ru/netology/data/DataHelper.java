package ru.netology.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.Locale;


public class DataHelper {
    private static final Faker faker = new Faker(Locale.ENGLISH);
    private static final Faker fakerWithCyrillicLocale = new Faker(new Locale("ru", "RU"));

    @Value
    public static class CardData {
        private final String number;
        private final String month;
        private final String year;
        private final String holder;
        private final String cvv;
    }

    public static final String APPROVED_CARD_NUMBER = "4444444444444441";
    public static final String DECLINED_CARD_NUMBER = "4444444444444442";
    private static final String EMPTY_STRING = "";


    public static String generateApprovedCardNumber() {
        return APPROVED_CARD_NUMBER;
    }

    public static String generateDeclinedCardNumber() {
        return DECLINED_CARD_NUMBER;
    }

    public static String generateValidOwner() {
        return faker.name().fullName().toUpperCase();
    }

    public static String generateInvalidOwner() {
        return faker.name().firstName().toUpperCase() + "-" + "al" + "-"
                + faker.name().lastName().toUpperCase();
    }

    public static String generateInvalidOwnerWithCyrillicSymbols() {
        return fakerWithCyrillicLocale.name().firstName().toUpperCase() + " "
                + fakerWithCyrillicLocale.name().lastName().toUpperCase();
    }

    public static String generateValidCVV() {
        return faker.numerify("###");
    }

    public static String generateInvalidCVVWith2Digit() {
        return faker.numerify("##");
    }

    public static String generateInvalidCVVWith4Digit() {
        return faker.numerify("####");
    }

    public static String generateMonth() {
        return LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String generateYear() {
        return LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String generateExpiredYear() {
        LocalDate currentDate = LocalDate.now();
        int currentYearShort = Integer.parseInt(currentDate.format(DateTimeFormatter.ofPattern("yy")));
        return String.format("%02d", currentYearShort - 1);
    }

    public static String generateInvalidYearZero() {
        return "00";
    }

    public static String generateInvalidMonth13() {
        return "13";
    }

    public static String generateInvalidMonthZero() {
        return "00";
    }

    public static String generateEmptyString() {
        return EMPTY_STRING;
    }

    public static String generateEmptyCardNumber() {
        return EMPTY_STRING;
    }

    private static String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static String generateInvalidCardNumberWithShortLength15() {
        int[] lengths = {15};
        int length = lengths[new Random().nextInt(lengths.length)];
        return generateRandomDigits(length);
    }

    public static String generateInvalidCardNumberWithLongLength() {
        Random random = new Random();
        int randomDigit = random.nextInt(10);
        return APPROVED_CARD_NUMBER + randomDigit;
    }

    public static String generateInvalidCardNumberWithLetters() {
        String[] invalidFormats = {"444444444444444A"};
        return invalidFormats[new Random().nextInt(invalidFormats.length)];
    }
}