package com.health.remind.test.data;

import com.alibaba.excel.EasyExcel;
import com.health.remind.test.data.entity.Nsdk100;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qtx
 * @since 2025/7/1 9:47
 */
public class TestData {

    @Test
    public void test() {
        double principal = 1000; // 初始本金
        double netValue = principal;
        double addPerDay = 50;
        double decreaseRate = 0.005;
        double increaseRate = 0.005;
        double decreaseLimit = 0.8; // 减少到80%
        double increaseLimit = 1.25; // 增加到125%
        int days = 0;
        double totalAdd = 0;

        // 阶段1：每天加50，资金每天-0.5%，直到减少20%
        for (double i = 1; i >= decreaseLimit; i -= decreaseRate) {
            netValue += addPerDay;
            totalAdd += addPerDay;
            netValue *= (1 - decreaseRate);
            days++;
        }
        int all = days / 5 * 2 + days;
        System.out.println("阶段1完成，经过 " + all + " 天");
        System.out.println("当前资金: " + netValue);
        double totalDecrease = totalAdd + principal;
        System.out.println("总加本金: " + totalDecrease);
        // 阶段2：资金每天+0.5%，直到增加25%
        for (double i = decreaseLimit; i <= increaseLimit; i += increaseRate) {
            netValue += addPerDay;
            totalAdd += addPerDay;
            netValue *= (1 + increaseRate);
            days++;
        }

        double profit = netValue - totalAdd - principal;
        int all2 = days / 5 * 2 + days;
        System.out.println("测试完成，经过 " + all2 + " 天");
        System.out.println("最终资金: " + netValue);
        double totalDecrease2 = totalAdd + principal;
        System.out.println("总加本金: " + totalDecrease2);
        System.out.println("盈利: " + profit);
        System.out.println("是否盈利: " + (profit > 0));
    }

    @Test
    public void test2() {
        String fileName = "src/test/resources/纳斯达克100指数历史数据 (1).xlsx";
        // 读取为 List<Test>
        List<Nsdk100> list = EasyExcel.read(fileName)
                .head(Nsdk100.class)
                .sheet()
                .doReadSync();
        // 处理读取的数据
        ArrayList<BigDecimal> arrayList = getBigDecimals(list);
        // 无脑定投模型
        BigDecimal netValue = new BigDecimal(100); // 初始资金
        BigDecimal addPerDay = new BigDecimal(100); // 每天投入资金
        BigDecimal totalAdd = new BigDecimal(100); // 投入资金
        BigDecimal max = BigDecimal.ZERO; // 最大盈利
        BigDecimal min = BigDecimal.ZERO; // 最大亏损
        BigDecimal yield = BigDecimal.ZERO; // 收益率
        for (BigDecimal bigDecimal : arrayList) {
            netValue = netValue.multiply(bigDecimal.add(new BigDecimal(1)))
                    .setScale(5, RoundingMode.HALF_UP);
            if (netValue.subtract(totalAdd)
                    .compareTo(BigDecimal.ZERO) > 0) {
                if (netValue.subtract(totalAdd)
                        .compareTo(max) > 0) {
                    max = netValue.subtract(totalAdd);
                }
            } else {
                if (netValue.subtract(totalAdd)
                        .compareTo(min) < 0) {
                    min = netValue.subtract(totalAdd);
                }
            }
            BigDecimal decimal = netValue.divide(totalAdd, 5,
                            RoundingMode.HALF_UP)
                    .subtract(new BigDecimal(1))
                    .multiply(new BigDecimal(100));
            if (decimal.compareTo(yield) > 0) {
                yield = decimal;
            }
            String x = "涨跌幅度：" + bigDecimal + ",当前资金: " + netValue + ", 总投入资金: " + totalAdd
                    + ", 盈利: " + netValue.subtract(
                    totalAdd) + ", 最大盈利: " + max + ", 最大亏损: " + min + "收益率：" + decimal;
            if (bigDecimal.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println(x);
            } else {
                System.err.println(x);
            }
            totalAdd = totalAdd.add(addPerDay);
            netValue = netValue.add(addPerDay);
        }
        System.out.println(
                "最终资金: " + netValue + ", 总投入资金: " + totalAdd + ", 盈利: " + netValue.subtract(totalAdd)
                        + ", 最大盈利: " + max + ", 最大亏损: " + min + ", 最大收益率: " + yield);
    }

    @Test
    public void test3() {
        String fileName = "src/test/resources/纳斯达克100指数历史数据 (1).xlsx";
        // 读取为 List<Test>
        List<Nsdk100> list = EasyExcel.read(fileName)
                .head(Nsdk100.class)
                .sheet()
                .doReadSync();
        // 处理读取的数据
        ArrayList<BigDecimal> arrayList = getBigDecimals(list);
        System.out.println("数据量：" + arrayList.size());
        // 梭哈模型
        BigDecimal netValue = new BigDecimal(30000); // 初始资金
        BigDecimal totalAdd = new BigDecimal(30000); // 投入资金
        BigDecimal max = BigDecimal.ZERO; // 最大盈利
        BigDecimal min = BigDecimal.ZERO; // 最大亏损
        BigDecimal yield = BigDecimal.ZERO; // 最大收益率
        for (BigDecimal bigDecimal : arrayList) {
            netValue = netValue.multiply(bigDecimal.add(new BigDecimal(1)))
                    .setScale(5, RoundingMode.HALF_UP);
            if (netValue.subtract(totalAdd)
                    .compareTo(BigDecimal.ZERO) > 0) {
                if (netValue.subtract(totalAdd)
                        .compareTo(max) > 0) {
                    max = netValue.subtract(totalAdd);
                }
            } else {
                if (netValue.subtract(totalAdd)
                        .compareTo(min) < 0) {
                    min = netValue.subtract(totalAdd);
                }
            }
            BigDecimal decimal = netValue.divide(totalAdd, 5,
                            RoundingMode.HALF_UP)
                    .subtract(new BigDecimal(1))
                    .multiply(new BigDecimal(100));
            if (decimal.compareTo(yield) > 0) {
                yield = decimal;
            }
            String x = "涨跌幅度：" + bigDecimal + ",当前资金: " + netValue + ", 总投入资金: " + totalAdd
                    + ", 盈利: " + netValue.subtract(
                    totalAdd) + ", 最大盈利: " + max + ", 最大亏损: " + min + "收益率：" + decimal;
            if (bigDecimal.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println(x);
            } else {
                System.err.println(x);
            }
        }
        System.out.println(
                "最终资金: " + netValue + ", 总投入资金: " + totalAdd + ", 盈利: " + netValue.subtract(totalAdd)
                        + ", 最大盈利: " + max + ", 最大亏损: " + min + ", 最大收益率: " + yield);
    }

    @NotNull
    private static ArrayList<BigDecimal> getBigDecimals(List<Nsdk100> list) {
        ArrayList<BigDecimal> arrayList = new ArrayList<>();
        for (Nsdk100 nsdk100 : list) {
            String price = nsdk100.getPrice();
            String replace = price.replace("%", "");
            BigDecimal divide = new BigDecimal(replace).divide(new BigDecimal(100), 5,
                    RoundingMode.HALF_UP);
/*            System.out.println("Date: " + nsdk100.getDate() + ", Start: " + nsdk100.getStart() +
                    ", End: " + nsdk100.getEnd() + ", High: " + nsdk100.getHigh() +
                    ", Low: " + nsdk100.getLow() + ", Trade: " + nsdk100.getTrade() +
                    ", Price: " + price + ",replace" + replace + ", Price Decimal: " + divide);*/
            arrayList.add(divide);
        }
        return arrayList;
    }
}
