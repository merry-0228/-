package com.pet.ui;

import com.pet.dao.*;
import com.pet.entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AdminMainFrame extends JFrame {
    private User currentUser;
    private JTabbedPane tabbedPane;

    public AdminMainFrame(User user) {
        this.currentUser = user;
        setTitle("宠物服务预约系统 - 管理员后台");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 顶部欢迎栏
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("欢迎您，管理员：" + currentUser.getNickname()));
        JButton logoutBtn = new JButton("退出登录");
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        topPanel.add(logoutBtn);
        add(topPanel, BorderLayout.NORTH);

        // 标签页
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("用户管理", createUserPanel());
        tabbedPane.addTab("店铺管理", createShopPanel());
        tabbedPane.addTab("服务管理", createServicePanel());
        tabbedPane.addTab("预约管理", createAppointmentPanel());
        tabbedPane.addTab("评价管理", createEvaluationPanel());
        tabbedPane.addTab("投诉处理", createComplaintPanel());
        tabbedPane.addTab("营收统计", createStatPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // 用户管理面板
    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "手机号", "昵称", "地址", "角色"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        // 刷新按钮
        JButton refreshBtn = new JButton("刷新");
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            try {
                List<User> users = new UserDao().findAllUsers();
                for (User u : users) {
                    model.addRow(new Object[]{u.getId(), u.getPhone(), u.getNickname(), u.getAddress(), u.getRole()});
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
            }
        });
        panel.add(refreshBtn, BorderLayout.SOUTH);

        // 初始化加载
        refreshBtn.doClick();
        return panel;
    }

    // 店铺管理面板
    private JPanel createShopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "商家ID", "店铺名称", "地址", "电话", "描述"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("刷新");
        JButton deleteBtn = new JButton("删除选中店铺");
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "请先选择要删除的店铺！");
                return;
            }
            int shopId = (int) model.getValueAt(row, 0);
            try {
                if (new ShopDao().deleteShop(shopId)) {
                    JOptionPane.showMessageDialog(this, "删除成功！");
                    refreshBtn.doClick();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(refreshBtn);
        btnPanel.add(deleteBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            try {
                List<Shop> shops = new ShopDao().findAllShops();
                for (Shop s : shops) {
                    model.addRow(new Object[]{s.getId(), s.getOwnerId(), s.getName(), s.getAddress(), s.getPhone(), s.getDescription()});
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
            }
        });

        refreshBtn.doClick();
        return panel;
    }

    // 服务管理面板
    private JPanel createServicePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "店铺ID", "服务名称", "类型", "价格", "描述"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("刷新");
        JButton deleteBtn = new JButton("删除选中服务");
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "请先选择要删除的服务！");
                return;
            }
            int serviceId = (int) model.getValueAt(row, 0);
            try {
                if (new ServiceItemDao().deleteService(serviceId)) {
                    JOptionPane.showMessageDialog(this, "删除成功！");
                    refreshBtn.doClick();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(refreshBtn);
        btnPanel.add(deleteBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            try {
                List<ServiceItem> services = new ServiceItemDao().findAllServices();
                for (ServiceItem s : services) {
                    model.addRow(new Object[]{s.getId(), s.getShopId(), s.getName(), s.getType(), s.getPrice(), s.getDescription()});
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
            }
        });

        refreshBtn.doClick();
        return panel;
    }

    // 预约管理面板
    private JPanel createAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "用户ID", "店铺ID", "服务ID", "预约时间", "状态", "支付状态", "价格"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("刷新");
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            try {
                List<Appointment> apps = new AppointmentDao().findAllAppointments();
                for (Appointment a : apps) {
                    model.addRow(new Object[]{a.getId(), a.getUserId(), a.getShopId(), a.getServiceId(), a.getAppointmentTime(), a.getStatus(), a.getPayStatus(), a.getPrice()});
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
            }
        });
        panel.add(refreshBtn, BorderLayout.SOUTH);

        refreshBtn.doClick();
        return panel;
    }

    // 评价管理面板
    private JPanel createEvaluationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "预约ID", "用户ID", "店铺ID", "内容", "评分", "时间"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("刷新");
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            try {
                List<Evaluation> evals = new EvaluationDao().findAllEvaluations();
                for (Evaluation eval : evals) {
                    model.addRow(new Object[]{eval.getId(), eval.getUserId(), eval.getContent(), eval.getScore(), eval.getCreateTime()});
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
            }
        });
        panel.add(refreshBtn, BorderLayout.SOUTH);

        refreshBtn.doClick();
        return panel;
    }

    // 投诉处理面板
    private JPanel createComplaintPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "用户ID", "店铺ID", "内容", "状态", "投诉时间", "处理结果"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("刷新");
        JButton handleBtn = new JButton("处理选中投诉");
        handleBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "请先选择要处理的投诉！");
                return;
            }
            int complaintId = (int) model.getValueAt(row, 0);
            String status = (String) model.getValueAt(row, 4);
            if ("handled".equals(status)) {
                JOptionPane.showMessageDialog(this, "该投诉已处理过了！");
                return;
            }
            String result = JOptionPane.showInputDialog(this, "请输入处理结果：");
            if (result == null || result.isEmpty()) return;
            try {
                if (new ComplaintDao().handleComplaint(complaintId, result)) {
                    JOptionPane.showMessageDialog(this, "处理成功！");
                    refreshBtn.doClick();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "处理失败：" + ex.getMessage());
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(refreshBtn);
        btnPanel.add(handleBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            try {
                List<Complaint> complaints = new ComplaintDao().findAllComplaints();
                for (Complaint c : complaints) {
                    model.addRow(new Object[]{c.getId(), c.getUserId(), c.getShopId(), c.getContent(), c.getStatus(), c.getCreateTime(), c.getHandleResult()});
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
            }
        });

        refreshBtn.doClick();
        return panel;
    }

    // 营收统计面板
    private JPanel createStatPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton calcBtn = new JButton("计算营收统计");
        JLabel totalLabel = new JLabel("总营收：0.0 元");
        JLabel countLabel = new JLabel("总预约数：0 单");
        JLabel vipDiscountLabel = new JLabel("VIP用户优惠总额：0.0 元");

        calcBtn.addActionListener(e -> {
            try {
                List<Appointment> apps = new AppointmentDao().findAllAppointments();
                double total = 0;
                int count = 0;
                double vipDiscount = 0;
                for (Appointment a : apps) {
                    if ("paid".equals(a.getPayStatus())) {
                        // 检查用户是否是VIP
                        User user = null;
                        List<User> users = new UserDao().findAllUsers();
                        for (User u : users) {
                            if (u.getId().equals(a.getUserId())) {
                                user = u;
                                break;
                            }
                        }
                        if (user != null && "vip".equals(user.getRole())) {
                            // VIP用户9折，计算优惠
                            double original = a.getPrice();
                            double discountPrice = original * 0.9;
                            total += discountPrice;
                            vipDiscount += original - discountPrice;
                        } else {
                            total += a.getPrice();
                        }
                        count++;
                    }
                }
                totalLabel.setText("总营收：" + String.format("%.2f", total) + " 元");
                countLabel.setText("总预约数：" + count + " 单");
                vipDiscountLabel.setText("VIP用户优惠总额：" + String.format("%.2f", vipDiscount) + " 元");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "统计失败：" + ex.getMessage());
            }
        });

        panel.add(calcBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(totalLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(countLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(vipDiscountLabel);

        return panel;
    }
}
