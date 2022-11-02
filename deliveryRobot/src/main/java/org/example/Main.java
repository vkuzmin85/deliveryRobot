package org.example;

public class Main {

    public static void main(String[] args) {

        final RobotDelivery robotDelivery = new RobotDelivery();
        for (int i = 1; i <= RobotDelivery.ROUTES_COUNT; i++) {
            new Thread(null, robotDelivery::calculateCount).start();
        }
        new Thread(null, robotDelivery::getStatistic).start();
    }

}