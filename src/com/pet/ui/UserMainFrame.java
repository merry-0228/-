package com.pet.ui;

import com.pet.dao.*;
import com.pet.entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserMainFrame extends JFrame {
    private User currentUser;
    private JTabbedPane tabbedPane;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public UserMainFrame(User user) {
        this.currentUser = user;
        setTitle("🐾 宠物服务预约系统 - 用户端");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 顶部面板
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(52, 152, 219));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel welcomeLabel = new JLabel("👋 欢迎您，" + currentUser.getNickname());
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        // 用户信息标签
        String roleText = "普通用户";
        if ("vip".equals(currentUser.getRole())) {
            roleText = "⭐ VIP用户（享9折优惠）";
        } else if ("admin".equals(currentUser.getRole())) {
            roleText = "🔧 管理员";
        }
        JLabel roleLabel = new JLabel(roleText);
        roleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        roleLabel.setForeground(new Color(255, 215, 0));
        topPanel.add(roleLabel, BorderLayout.CENTER);

        // 按钮面板
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton profileBtn = new JButton("👤 个人信息");
        profileBtn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        profileBtn.addActionListener(e -> showProfileDialog());

        JButton switchBtn = new JButton("🔄 切换账号");
        switchBtn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        switchBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "确定要切换账号吗？", "切换账号", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });

        JButton logoutBtn = new JButton("🚪 退出");
        logoutBtn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "确定要退出系统吗？", "退出", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        btnPanel.add(profileBtn);
        btnPanel.add(switchBtn);
        btnPanel.add(logoutBtn);
        topPanel.add(btnPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 标签页
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        tabbedPane.addTab("🏪 浏览店铺", createShopBrowsePanel());
        tabbedPane.addTab("🐕 我的宠物", createPetPanel());
        tabbedPane.addTab("📅 我的预约", createMyAppointmentPanel());
        tabbedPane.addTab("📝 提交投诉", createComplaintPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // 个人信息对话框
    private void showProfileDialog() {
        JDialog dialog = new JDialog(this, "个人信息", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 手机号（不可修改）
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("手机号："), gbc);
        gbc.gridx = 1;
        JTextField phoneField = new JTextField(currentUser.getPhone());
        phoneField.setEditable(false);
        phoneField.setBackground(Color.LIGHT_GRAY);
        panel.add(phoneField, gbc);

        // 昵称
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("昵称："), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(currentUser.getNickname());
        panel.add(nameField, gbc);

        // 地址
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("地址："), gbc);
        gbc.gridx = 1;
        JTextField addressField = new JTextField(currentUser.getAddress());
        panel.add(addressField, gbc);

        // 新密码
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("新密码："), gbc);
        gbc.gridx = 1;
        JPasswordField pwdField = new JPasswordField();
        pwdField.setToolTipText("不修改请留空");
        panel.add(pwdField, gbc);

        // 确认密码
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("确认密码："), gbc);
        gbc.gridx = 1;
        JPasswordField confirmPwdField = new JPasswordField();
        panel.add(confirmPwdField, gbc);

        // 按钮
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton saveBtn = new JButton("保存修改");
        saveBtn.setBackground(new Color(52, 152, 219));
        saveBtn.setForeground(Color.WHITE);
        JButton cancelBtn = new JButton("取消");
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        panel.add(btnPanel, gbc);

        saveBtn.addActionListener(evt -> {
            String nickname = nameField.getText().trim();
            String address = addressField.getText().trim();
            String password = new String(pwdField.getPassword()).trim();
            String confirmPwd = new String(confirmPwdField.getPassword()).trim();

            if (nickname.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "昵称不能为空！");
                return;
            }

            try {
                UserDao dao = new UserDao();
                User updatedUser = dao.findUserById(currentUser.getId());
                updatedUser.setNickname(nickname);
                updatedUser.setAddress(address);

                if (!password.isEmpty()) {
                    if (!password.equals(confirmPwd)) {
                        JOptionPane.showMessageDialog(dialog, "两次密码输入不一致！");
                        return;
                    }
                    updatedUser.setPassword(password);
                }

                if (dao.updateUser(updatedUser)) {
                    JOptionPane.showMessageDialog(dialog, "✅ 个人信息更新成功！");
                    currentUser = updatedUser;
                    // 更新界面标题
                    setTitle("🐾 宠物服务预约系统 - " + currentUser.getNickname());
                    dialog.dispose();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "更新失败：" + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(evt -> dialog.dispose());

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // 浏览店铺面板（增加搜索功能）
    private JPanel createShopBrowsePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 搜索栏
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchPanel.add(new JLabel("🔍 搜索："));
        JTextField searchField = new JTextField(15);
        searchField.setToolTipText("输入店铺名称或地址搜索");
        searchPanel.add(searchField);
        JButton searchBtn = new JButton("搜索");
        searchBtn.setBackground(new Color(52, 152, 219));
        searchBtn.setForeground(Color.WHITE);
        searchPanel.add(searchBtn);
        JButton resetBtn = new JButton("显示全部");
        searchPanel.add(resetBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // 表格
        String[] columns = {"ID", "店铺名称", "地址", "电话", "描述", "评分"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 12));
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        // 按钮面板
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton bookBtn = new JButton("📋 预约该店铺服务");
        bookBtn.setBackground(new Color(46, 204, 113));
        bookBtn.setForeground(Color.WHITE);
        JButton refreshBtn = new JButton("🔄 刷新");

        btnPanel.add(bookBtn);
        btnPanel.add(refreshBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // 加载数据
        Runnable loadData = () -> {
            model.setRowCount(0);
            try {
                List<Shop> shops = new ShopDao().findAllShops();
                String keyword = searchField.getText().trim().toLowerCase();
                for (Shop s : shops) {
                    if (!keyword.isEmpty() && !s.getName().toLowerCase().contains(keyword)
                            && !s.getAddress().toLowerCase().contains(keyword)) {
                        continue;
                    }
                    // 计算平均评分
                    double avgScore = calculateShopAvgScore(s.getId());
                    String scoreStr = avgScore > 0 ? String.format("%.1f", avgScore) : "暂无";
                    model.addRow(new Object[]{s.getId(), s.getName(), s.getAddress(),
                            s.getPhone(), s.getDescription(), scoreStr});
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
            }
        };

        searchBtn.addActionListener(evt -> loadData.run());
        resetBtn.addActionListener(evt -> {
            searchField.setText("");
            loadData.run();
        });
        refreshBtn.addActionListener(evt -> loadData.run());

        bookBtn.addActionListener(evt -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "请先选择要预约的店铺！");
                return;
            }
            int shopId = (int) model.getValueAt(row, 0);
            String shopName = (String) model.getValueAt(row, 1);
            openBookDialog(shopId, shopName);
        });

        loadData.run();
        return panel;
    }

    // 计算店铺平均评分
    private double calculateShopAvgScore(Integer shopId) {
        try {
            List<Evaluation> evals = new EvaluationDao().findEvaluationsByShopId(shopId);
            if (evals.isEmpty()) return 0;
            double sum = 0;
            for (Evaluation e : evals) {
                sum += e.getScore();
            }
            return sum / evals.size();
        } catch (SQLException e) {
            return 0;
        }
    }

    // 预约对话框（增强版）
    private void openBookDialog(Integer shopId, String shopName) {
        JDialog dialog = new JDialog(this, "📋 预约服务 - " + shopName, true);
        dialog.setSize(600, 450);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 服务列表
        String[] columns = {"ID", "服务名称", "类型", "原价", "您的价格", "描述"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        // 底部面板 - 预约时间和操作
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        bottomPanel.add(new JLabel("📅 预约时间："), gbc);

        gbc.gridx = 1;
        // 使用Spinner选择日期时间
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.HOUR_OF_DAY);
        JSpinner timeSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "yyyy-MM-dd HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setPreferredSize(new Dimension(180, 30));
        bottomPanel.add(timeSpinner, gbc);

        gbc.gridx = 2;
        JButton bookBtn = new JButton("✅ 确认预约");
        bookBtn.setBackground(new Color(46, 204, 113));
        bookBtn.setForeground(Color.WHITE);
        bottomPanel.add(bookBtn, gbc);

        gbc.gridx = 3;
        JButton cancelBtn = new JButton("❌ 取消");
        bottomPanel.add(cancelBtn, gbc);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        // 加载服务列表
        try {
            List<ServiceItem> services = new ServiceItemDao().findServicesByShopId(shopId);
            boolean isVip = "vip".equals(currentUser.getRole());
            for (ServiceItem s : services) {
                double finalPrice = isVip ? s.getPrice() * 0.9 : s.getPrice();
                model.addRow(new Object[]{
                        s.getId(), s.getName(), s.getType(),
                        String.format("%.2f", s.getPrice()),
                        String.format("%.2f", finalPrice),
                        s.getDescription()
                });
            }
            if (services.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "该店铺暂无服务项目！");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "加载服务失败：" + ex.getMessage());
        }

        bookBtn.addActionListener(evt -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "请选择要预约的服务！");
                return;
            }
            int serviceId = (int) model.getValueAt(row, 0);
            String serviceName = (String) model.getValueAt(row, 1);
            double price = Double.parseDouble((String) model.getValueAt(row, 4));
            Date time = (Date) timeSpinner.getValue();

            if (time.before(new Date())) {
                JOptionPane.showMessageDialog(dialog, "⚠️ 预约时间不能是过去的时间！");
                return;
            }

            try {
                Appointment app = new Appointment(currentUser.getId(), shopId, serviceId, time, price);
                if (new AppointmentDao().addAppointment(app)) {
                    JOptionPane.showMessageDialog(dialog,
                            "✅ 预约成功！\n服务：" + serviceName + "\n时间：" + sdf.format(time) +
                                    "\n价格：" + String.format("%.2f", price) + "元" +
                                    ("vip".equals(currentUser.getRole()) ? "\n⭐ VIP已享9折优惠" : ""));
                    dialog.dispose();
                    tabbedPane.setSelectedIndex(2);
                    // 刷新预约列表
                    refreshAppointments();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "预约失败：" + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(evt -> dialog.dispose());

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // 我的宠物面板（增加修改功能）
    private JPanel createPetPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"ID", "宠物名字", "品种", "年龄", "性别", "出生日期", "创建时间"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 12));
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("添加/修改宠物"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("名字："), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(10);
        nameField.setToolTipText("请输入宠物名字");
        formPanel.add(nameField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("品种："), gbc);
        gbc.gridx = 3;
        JTextField breedField = new JTextField(10);
        breedField.setToolTipText("请输入宠物品种");
        formPanel.add(breedField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("性别："), gbc);
        gbc.gridx = 1;
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"公", "母"});
        formPanel.add(genderCombo, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("出生日期："), gbc);
        gbc.gridx = 3;
        // 使用日期选择器
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        JSpinner birthSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(birthSpinner, "yyyy-MM-dd");
        birthSpinner.setEditor(dateEditor);
        birthSpinner.setPreferredSize(new Dimension(120, 25));
        birthSpinner.setToolTipText("选择宠物出生日期，自动计算年龄");
        formPanel.add(birthSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.NONE;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton addBtn = new JButton("➕ 添加宠物");
        addBtn.setBackground(new Color(46, 204, 113));
        addBtn.setForeground(Color.WHITE);
        JButton updateBtn = new JButton("✏️ 修改宠物");
        updateBtn.setBackground(new Color(52, 152, 219));
        updateBtn.setForeground(Color.WHITE);
        JButton deleteBtn = new JButton("🗑️ 删除");
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        JButton refreshBtn = new JButton("🔄 刷新");

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);
        formPanel.add(btnPanel, gbc);

        panel.add(formPanel, BorderLayout.NORTH);

        // 加载数据
        Runnable loadData = () -> {
            model.setRowCount(0);
            try {
                List<Pet> pets = new PetDao().findPetsByUserId(currentUser.getId());
                for (Pet p : pets) {
                    String ageDisplay = p.getAgeDisplay();
                    String birthStr = p.getBirthDate() != null ?
                            new SimpleDateFormat("yyyy-MM-dd").format(p.getBirthDate()) : "-";
                    model.addRow(new Object[]{
                            p.getId(), p.getName(), p.getBreed(), ageDisplay,
                            p.getGender(), birthStr,
                            p.getCreateTime() != null ? new SimpleDateFormat("yyyy-MM-dd").format(p.getCreateTime()) : "-"
                    });
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
            }
        };

        // 添加宠物
        addBtn.addActionListener(evt -> {
            String name = nameField.getText().trim();
            String breed = breedField.getText().trim();
            String gender = (String) genderCombo.getSelectedItem();
            Date birthDate = (Date) birthSpinner.getValue();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ 宠物名字不能为空！");
                nameField.requestFocus();
                return;
            }

            try {
                Pet pet = new Pet();
                pet.setUserId(currentUser.getId());
                pet.setName(name);
                pet.setBreed(breed);
                pet.setGender(gender);
                pet.setBirthDate(birthDate);
                pet.setCreateTime(new Date());

                if (new PetDao().addPet(pet)) {
                    JOptionPane.showMessageDialog(this, "✅ 宠物档案添加成功！");
                    loadData.run();
                    nameField.setText("");
                    breedField.setText("");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "添加失败：" + ex.getMessage());
            }
        });

        // 修改宠物
        updateBtn.addActionListener(evt -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "请先选择要修改的宠物！");
                return;
            }
            int petId = (int) model.getValueAt(row, 0);
            String name = nameField.getText().trim();
            String breed = breedField.getText().trim();
            String gender = (String) genderCombo.getSelectedItem();
            Date birthDate = (Date) birthSpinner.getValue();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "宠物名字不能为空！");
                return;
            }

            try {
                Pet pet = new Pet();
                pet.setId(petId);
                pet.setName(name);
                pet.setBreed(breed);
                pet.setGender(gender);
                pet.setBirthDate(birthDate);

                if (new PetDao().updatePet(pet)) {
                    JOptionPane.showMessageDialog(this, "✅ 宠物信息更新成功！");
                    loadData.run();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "更新失败：" + ex.getMessage());
            }
        });

        // 删除宠物
        deleteBtn.addActionListener(evt -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "请先选择要删除的宠物！");
                return;
            }
            int petId = (int) model.getValueAt(row, 0);
            String petName = (String) model.getValueAt(row, 1);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "确定要删除宠物 \"" + petName + "\" 吗？", "确认删除",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (new PetDao().deletePet(petId)) {
                        JOptionPane.showMessageDialog(this, "✅ 删除成功！");
                        loadData.run();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
                }
            }
        });

        // 点击表格行自动填充表单
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    nameField.setText((String) model.getValueAt(row, 1));
                    breedField.setText((String) model.getValueAt(row, 2));
                    String gender = (String) model.getValueAt(row, 4);
                    genderCombo.setSelectedItem(gender);
                    // 解析出生日期
                    String birthStr = (String) model.getValueAt(row, 5);
                    if (!"-".equals(birthStr)) {
                        try {
                            Date birth = new SimpleDateFormat("yyyy-MM-dd").parse(birthStr);
                            birthSpinner.setValue(birth);
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                }
            }
        });

        refreshBtn.addActionListener(evt -> loadData.run());
        loadData.run();
        return panel;
    }

    // 我的预约面板
    private JPanel createMyAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"ID", "店铺ID", "服务ID", "预约时间", "状态", "支付状态", "价格", "操作"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 12));
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        // 按钮面板
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton payBtn = new JButton("💰 支付");
        payBtn.setBackground(new Color(46, 204, 113));
        payBtn.setForeground(Color.WHITE);
        JButton evalBtn = new JButton("⭐ 评价");
        evalBtn.setBackground(new Color(52, 152, 219));
        evalBtn.setForeground(Color.WHITE);
        JButton cancelBtn = new JButton("❌ 取消预约");
        cancelBtn.setBackground(new Color(231, 76, 60));
        cancelBtn.setForeground(Color.WHITE);
        JButton refreshBtn = new JButton("🔄 刷新");

        btnPanel.add(payBtn);
        btnPanel.add(evalBtn);
        btnPanel.add(cancelBtn);
        btnPanel.add(refreshBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // 加载数据
        Runnable loadData = () -> {
            model.setRowCount(0);
            try {
                List<Appointment> apps = new AppointmentDao().findAppointmentsByUserId(currentUser.getId());
                for (Appointment a : apps) {
                    String statusText = getStatusText(a.getStatus());
                    String payText = "paid".equals(a.getPayStatus()) ? "✅ 已支付" : "⏳ 未支付";
                    String operation = getOperationText(a.getStatus(), a.getPayStatus());
                    model.addRow(new Object[]{
                            a.getId(), a.getShopId(), a.getServiceId(),
                            sdf.format(a.getAppointmentTime()), statusText,
                            payText, String.format("%.2f", a.getPrice()), operation
                    });
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
            }
        };

        // 支付
        payBtn.addActionListener(evt -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "请选择要支付的预约！");
                return;
            }
            int appId = (int) model.getValueAt(row, 0);
            String payStatus = (String) model.getValueAt(row, 5);
            double price = Double.parseDouble((String) model.getValueAt(row, 6));

            if ("✅ 已支付".equals(payStatus)) {
                JOptionPane.showMessageDialog(this, "该预约已支付！");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "确认支付 " + String.format("%.2f", price) + " 元？", "支付确认",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (new AppointmentDao().updatePayStatus(appId, "paid")) {
                        JOptionPane.showMessageDialog(this, "✅ 支付成功！");
                        loadData.run();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "支付失败：" + ex.getMessage());
                }
            }
        });

        // 评价
        evalBtn.addActionListener(evt -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "请选择要评价的预约！");
                return;
            }
            int appId = (int) model.getValueAt(row, 0);
            int shopId = (int) model.getValueAt(row, 1);
            String status = (String) model.getValueAt(row, 4);

            if (!"✅ 已完成".equals(status)) {
                JOptionPane.showMessageDialog(this, "只有完成的服务才能评价！");
                return;
            }

            // 检查是否已评价
            try {
                List<Evaluation> evals = new EvaluationDao().findEvaluationsByShopId(shopId);
                for (Evaluation eval : evals) {
                    if (eval.getAppointmentId().equals(appId)) {
                        JOptionPane.showMessageDialog(this, "该预约已评价过了！");
                        return;
                    }
                }
            } catch (SQLException ex) {
                // ignore
            }

            // 评价对话框
            JDialog evalDialog = new JDialog(this, "评价服务", true);
            evalDialog.setSize(400, 300);
            evalDialog.setLocationRelativeTo(this);

            JPanel evalPanel = new JPanel(new GridBagLayout());
            evalPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0;
            gbc.gridy = 0;
            evalPanel.add(new JLabel("评分："), gbc);
            gbc.gridx = 1;
            JComboBox<Integer> scoreCombo = new JComboBox<>(new Integer[]{5, 4, 3, 2, 1});
            evalPanel.add(scoreCombo, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            evalPanel.add(new JLabel("评价内容："), gbc);
            gbc.gridx = 1;
            JTextArea contentArea = new JTextArea(5, 20);
            contentArea.setLineWrap(true);
            JScrollPane contentScroll = new JScrollPane(contentArea);
            evalPanel.add(contentScroll, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            JButton submitBtn = new JButton("提交评价");
            submitBtn.setBackground(new Color(52, 152, 219));
            submitBtn.setForeground(Color.WHITE);
            evalPanel.add(submitBtn, gbc);

            submitBtn.addActionListener(ev -> {
                String content = contentArea.getText().trim();
                if (content.isEmpty()) {
                    JOptionPane.showMessageDialog(evalDialog, "请填写评价内容！");
                    return;
                }
                int score = (int) scoreCombo.getSelectedItem();
                try {
                    Evaluation eval = new Evaluation(appId, currentUser.getId(), shopId, content, score);
                    if (new EvaluationDao().addEvaluation(eval)) {
                        JOptionPane.showMessageDialog(evalDialog, "✅ 评价成功！");
                        evalDialog.dispose();
                        loadData.run();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(evalDialog, "评价失败：" + ex.getMessage());
                }
            });

            evalDialog.add(evalPanel);
            evalDialog.setVisible(true);
        });

        // 取消预约
        cancelBtn.addActionListener(evt -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "请选择要取消的预约！");
                return;
            }
            int appId = (int) model.getValueAt(row, 0);
            String status = (String) model.getValueAt(row, 4);

            if ("✅ 已完成".equals(status) || "❌ 已取消".equals(status)) {
                JOptionPane.showMessageDialog(this, "该预约无法取消！");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "确定要取消该预约吗？", "确认取消",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (new AppointmentDao().updateAppointmentStatus(appId, "cancelled")) {
                        JOptionPane.showMessageDialog(this, "✅ 取消成功！");
                        loadData.run();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "取消失败：" + ex.getMessage());
                }
            }
        });

        refreshBtn.addActionListener(evt -> loadData.run());
        loadData.run();
        return panel;
    }

    private void refreshAppointments() {
        // 刷新预约列表
        if (tabbedPane.getSelectedIndex() == 2) {
            // 重新加载预约面板
            JPanel panel = (JPanel) tabbedPane.getComponentAt(2);
            // 触发刷新
        }
        tabbedPane.setSelectedIndex(2);
    }

    private String getStatusText(String status) {
        switch (status) {
            case "pending": return "⏳ 待接单";
            case "accepted": return "📋 已接单";
            case "completed": return "✅ 已完成";
            case "cancelled": return "❌ 已取消";
            default: return status;
        }
    }

    private String getOperationText(String status, String payStatus) {
        if ("cancelled".equals(status)) return "-";
        if ("completed".equals(status)) {
            if ("paid".equals(payStatus)) return "✅ 已支付";
            return "💰 待支付";
        }
        if ("pending".equals(status)) return "⏳ 待接单";
        return "📋 处理中";
    }

    // 提交投诉面板
    private JPanel createComplaintPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("🏪 店铺ID："), gbc);
        gbc.gridx = 1;
        JTextField shopIdField = new JTextField(15);
        shopIdField.setToolTipText("请输入要投诉的店铺ID");
        panel.add(shopIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("📝 投诉内容："), gbc);
        gbc.gridx = 1;
        JTextArea contentArea = new JTextArea(8, 20);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(contentArea);
        panel.add(scroll, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        JButton submitBtn = new JButton("📤 提交投诉");
        submitBtn.setBackground(new Color(231, 76, 60));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setPreferredSize(new Dimension(120, 40));
        panel.add(submitBtn, gbc);

        submitBtn.addActionListener(evt -> {
            String shopIdStr = shopIdField.getText().trim();
            String content = contentArea.getText().trim();

            if (shopIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ 请输入店铺ID！");
                shopIdField.requestFocus();
                return;
            }
            if (content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ 请输入投诉内容！");
                contentArea.requestFocus();
                return;
            }

            int shopId;
            try {
                shopId = Integer.parseInt(shopIdStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "⚠️ 店铺ID必须是数字！");
                return;
            }

            try {
                // 检查店铺是否存在
                Shop shop = new ShopDao().findAllShops().stream()
                        .filter(s -> s.getId().equals(shopId))
                        .findFirst().orElse(null);
                if (shop == null) {
                    JOptionPane.showMessageDialog(this, "❌ 该店铺不存在！");
                    return;
                }

                Complaint complaint = new Complaint(currentUser.getId(), shopId, content);
                if (new ComplaintDao().addComplaint(complaint)) {
                    JOptionPane.showMessageDialog(this, "✅ 投诉提交成功！管理员会尽快处理");
                    shopIdField.setText("");
                    contentArea.setText("");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "提交失败：" + ex.getMessage());
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 测试用
            User testUser = new User();
            testUser.setId(1);
            testUser.setNickname("测试用户");
            testUser.setPhone("user");
            testUser.setRole("user");
            new UserMainFrame(testUser).setVisible(true);
        });
    }
}