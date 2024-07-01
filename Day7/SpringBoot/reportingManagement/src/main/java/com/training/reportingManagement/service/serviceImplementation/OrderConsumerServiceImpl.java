package com.training.reportingManagement.service.serviceImplementation;

import com.training.reportingManagement.dao.entity.Order;
import com.training.reportingManagement.dao.entity.Order_Details;
import com.training.reportingManagement.service.serviceInterface.EmailService;
import com.training.reportingManagement.service.serviceInterface.OrderConsumerService;
import com.training.reportingManagement.service.serviceInterface.OrderDetailsService;
import com.training.reportingManagement.service.serviceInterface.OrderItemsService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class OrderConsumerServiceImpl implements OrderConsumerService {

    @Autowired
    private OrderDetailsService orderDetailsService;

    @Autowired
    private OrderItemsService orderItemsService;

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "Orders", groupId = "${kafka.consumer.groupId}", containerFactory = "kafkaListenerContainerFactory")
    @Override
    public void consumerOrder(Order order) {
        Order_Details orderDetails = orderDetailsService.addOrder(order);
        orderItemsService.addOrder(order, orderDetails);
        System.out.println("Received order: " + order);
        saveOrderToExcel(order);
        String emailSubject = "Order Confirmation - Order ID: " + order.getId();
        String emailBody = "Dear Customer,\n\nThank you for your order.\n\nOrder ID: " + order.getId() +
                "\nTotal Quantity: " + order.getTotalProducts() +
                "\nTotal Price: " + order.getTotalPrice() +
                "\n\nBest regards,\nBliBli";
        emailService.sendSimpleMessage(order.getUserId(), emailSubject, emailBody);
    }

    private void saveOrderToExcel(Order order) {
        String filePath = "OrderDetails.xlsx";
        Workbook workbook = null;
        Sheet sheet = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            File file = new File(filePath);

            if (file.exists()) {
                fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheetAt(0);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Order Details");

                Row headerRow = sheet.createRow(0);
                Cell headerCell1 = headerRow.createCell(0);
                headerCell1.setCellValue("Order ID");
                Cell headerCell2 = headerRow.createCell(1);
                headerCell2.setCellValue("Email ID");
                Cell headerCell3 = headerRow.createCell(2);
                headerCell3.setCellValue("Quantity");
                Cell headerCell4 = headerRow.createCell(3);
                headerCell4.setCellValue("Total Price");
                Cell headerCell5 = headerRow.createCell(4);
                headerCell5.setCellValue("Ordered On");
            }
            int rowCount = sheet.getLastRowNum();
            Row row = sheet.createRow(++rowCount);
            row.createCell(0).setCellValue(order.getId());
            row.createCell(1).setCellValue(order.getUserId());
            row.createCell(2).setCellValue(order.getTotalProducts());
            row.createCell(3).setCellValue(order.getTotalPrice());
            Cell dateCell = row.createCell(4);
            dateCell.setCellValue(LocalDate.now().format(DateTimeFormatter.ISO_DATE));

            fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
