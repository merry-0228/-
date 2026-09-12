package com.pet.ui;

import com.pet.dao.*;
import com.pet.entity.*;
import com.pet.util.DBUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShopMainFrame extends JFrame {
    private User currentUser;
    private Shop currentShop;
    private JTabbedPane tabbedPane;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public ShopMainFrame(User user) {
        this.currentUser = user;
        // 先查询商家的店铺
        try {
            currentShop = new ShopDao().findShopByOwnerId(user.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setTitle("🐾 宠物服务预约系统 - 商家后台");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 顶部欢迎栏
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(52, 152, 219));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel welcomeLabel = new JLabel("👋 欢迎您，商家：" + currentUser.getNickname());
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel, BorderLayout.WEST);

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
        tabbedPane.addTab("🏪 店铺信息", createShopInfoPanel());
        tabbedPane.addTab("📋 服务管理", createServicePanel());
        tabbedPane.addTab("📅 预约管理", createAppointmentPanel());
        tabbedPane.addTab("⭐ 评价查看", createEvaluationPanel());
        tabbedPane.addTab("💰 我的收入", createIncomePanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ==================== 个人信息对话框 ====================
    private void showProfileDialog() {
        JDialog dialog = new JDialog(this, "个人信息", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("手机号："), gbc);
        gbc.gridx = 1;
        JTextField phoneField = new JTextField(currentUser.getPhone());
        phoneField.setEditable(false);
        phoneField.setBackground(Color.LIGHT_GRAY);
        panel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("昵称："), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(currentUser.getNickname());
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("地址："), gbc);
        gbc.gridx = 1;
        JTextField addressField = new JTextField(currentUser.getAddress());
        panel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("新密码："), gbc);
        gbc.gridx = 1;
        JPasswordField pwdField = new JPasswordField();
        pwdField.setToolTipText("不修改请留空");
        panel.add(pwdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("确认密码："), gbc);
        gbc.gridx = 1;
        JPasswordField confirmPwdField = new JPasswordField();
        panel.add(confirmPwdField, gbc);

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

    // ==================== 店铺信息面板 ====================
    private JPanel createShopInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(20);
        nameField.setToolTipText("请输入店铺名称");
        JTextField addressField = new JTextField(20);
        addressField.setToolTipText("请输入店铺地址");
        JTextField phoneField = new JTextField(20);
        phoneField.setToolTipText("请输入联系电话");
        JTextArea descArea = new JTextArea(4, 20);
        descArea.setLineWrap(true);
        descArea.setToolTipText("请输入店铺描述信息");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("店铺名称："), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("店铺地址："), gbc);
        gbc.gridx = 1;
        panel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("联系电话："), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("店铺描述："), gbc);
        gbc.gridx = 1;
        JScrollPane scroll = new JScrollPane(descArea);
        scroll.setPreferredSize(new Dimension(300, 80));
        panel.add(scroll, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        JButton saveBtn = new JButton("💾 保存店铺信息");
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setPreferredSize(new Dimension(140, 35));

        JButton updateBtn = new JButton("✏️ 修改店铺信息");
        updateBtn.setBackground(new Color(52, 152, 219));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setPreferredSize(new Dimension(140, 35));

        btnPanel.add(saveBtn);
        btnPanel.add(updateBtn);
        panel.add(btnPanel, gbc);

        // 如果已有店铺，填充数据
        if (currentShop != null) {
            nameField.setText(currentShop.getName());
            addressField.setText(currentShop.getAddress());
            phoneField.setText(currentShop.getPhone());
            descArea.setText(currentShop.getDescription());
        }

        // 保存店铺（首次创建）
        saveBtn.addActionListener(evt -> {
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();
            String desc = descArea.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ 店铺名称不能为空！");
                nameField.requestFocus();
                return;
            }
            if (address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ 店铺地址不能为空！");
                addressField.requestFocus();
                return;
            }
            if (phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ 联系电话不能为空！");
                phoneField.requestFocus();
                return;
            }

            try {
                ShopDao shopDao = new ShopDao();
                if (currentShop == null) {
                    Shop shop = new Shop(currentUser.getId(), name, address, phone, desc);
                    if (shopDao.addShop(shop)) {
                        JOptionPane.showMessageDialog(this, "✅ 店铺信息保存成功！");
                        currentShop = shopDao.findShopByOwnerId(currentUser.getId());
                        // 刷新服务管理面板
                        int serviceIndex = tabbedPane.indexOfTab("📋 服务管理");
                        if (serviceIndex >= 0) {
                            tabbedPane.removeTabAt(serviceIndex);
                            tabbedPane.insertTab("📋 服务管理", null, createServicePanel(), null, serviceIndex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "店铺信息已存在，点击'修改店铺信息'按钮进行修改");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "保存失败：" + ex.getMessage());
            }
        });

        // 修改店铺信息
        updateBtn.addActionListener(evt -> {
            if (currentShop == null) {
                JOptionPane.showMessageDialog(this, "请先创建店铺！");
                return;
            }

            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();
            String desc = descArea.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ 店铺名称不能为空！");
                nameField.requestFocus();
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "确认修改店铺信息？", "确认修改", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            try {
                // 直接更新数据库
                String sql = "UPDATE shop SET name=?, address=?, phone=?, description=? WHERE id=?";
                java.sql.Connection conn = DBUtil.getConnection();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, name);
                pstmt.setString(2, address);
                pstmt.setString(3, phone);
                pstmt.setString(4, desc);
                pstmt.setInt(5, currentShop.getId());
                int rows = pstmt.executeUpdate();
                pstmt.close();
                conn.close();

                if (rows > 0) {
                    currentShop.setName(name);
                    currentShop.setAddress(address);
                    currentShop.setPhone(phone);
                    currentShop.setDescription(desc);
                    JOptionPane.showMessageDialog(this, "✅ 店铺信息更新成功！");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "更新失败：" + ex.getMessage());
            }
        });

        return panel;
    }

    // ==================== 服务管理面板（增强版） ====================
    private JPanel createServicePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (currentShop == null) {
            JLabel label = new JLabel("⚠️ 请先完善店铺信息才能添加服务！", JLabel.CENTER);
            label.setFont(new Font("微软雅黑", Font.BOLD, 16));
            label.setForeground(Color.RED);
            panel.add(label, BorderLayout.CENTER);
            return panel;
        }

        // ====== 表单面板（带输入提示） ======
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219)),
                "📝 添加/修改服务",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("微软雅黑", Font.BOLD, 13)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("服务名称："), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(12);
        nameField.setToolTipText("请输入服务名称，如：洗澡、美容、寄养");
        formPanel.add(nameField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("服务类型："), gbc);
        gbc.gridx = 3;
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"洗护", "美容", "寄养", "医疗", "其他"});
        typeCombo.setToolTipText("选择服务类型");
        formPanel.add(typeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("服务价格："), gbc);
        gbc.gridx = 1;
        JTextField priceField = new JTextField(12);
        priceField.setToolTipText("请输入价格（数字），如：60.00");
        formPanel.add(priceField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("服务描述："), gbc);
        gbc.gridx = 3;
        JTextField descField = new JTextField(12);
        descField.setToolTipText("请输入服务描述（可选）");
        formPanel.add(descField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton addBtn = new JButton("➕ 添加服务");
        addBtn.setBackground(new Color(46, 204, 113));
        addBtn.setForeground(Color.WHITE);
        addBtn.setToolTipText("添加新服务到店铺");
        JButton updateBtn = new JButton("✏️ 修改服务");
        updateBtn.setBackground(new Color(52, 152, 219));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setToolTipText("修改选中的服务信息");
        JButton deleteBtn = new JButton("🗑️ 删除服务");
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setToolTipText("删除选中的服务");
        JButton clearBtn = new JButton("🔄 清空表单");
        clearBtn.setToolTipText("清空输入框内容");

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(clearBtn);
        formPanel.add(btnPanel, gbc);

        panel.add(formPanel, BorderLayout.NORTH);

        // ====== 服务列表表格 ======
        String[] columns = {"ID", "服务名称", "类型", "价格", "描述"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        // ====== 底部刷新按钮 ======
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JLabel countLabel = new JLabel("共 0 项服务");
        countLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        JButton refreshBtn = new JButton("🔄 刷新列表");
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        bottomPanel.add(countLabel);
        bottomPanel.add(refreshBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // ====== 加载数据 ======
        Runnable loadData = () -> {
            model.setRowCount(0);
            try {
                List<ServiceItem> services = new ServiceItemDao().findServicesByShopId(currentShop.getId());
                for (ServiceItem s : services) {
                    model.addRow(new Object[]{
                            s.getId(),
                            s.getName(),
                            s.getType(),
                            String.format("%.2f", s.getPrice()),
                            s.getDescription() != null ? s.getDescription() : ""
                    });
                }
                countLabel.setText("共 " + services.size() + " 项服务");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
            }
        };

        // ====== 添加服务 ======
        addBtn.addActionListener(evt -> {
            String name = nameField.getText().trim();
            String priceStr = priceField.getText().trim();
            String type = (String) typeCombo.getSelectedItem();
            String desc = descField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ 服务名称不能为空！");
                nameField.requestFocus();
                return;
            }
            if (priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ 服务价格不能为空！");
                priceField.requestFocus();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    JOptionPane.showMessageDialog(this, "⚠️ 价格必须大于0！");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "⚠️ 价格必须是数字！");
                priceField.requestFocus();
                return;
            }

            try {
                ServiceItem service = new ServiceItem(currentShop.getId(), name, type, price, desc);
                if (new ServiceItemDao().addService(service)) {
                    JOptionPane.showMessageDialog(this, "✅ 服务添加成功！");
                    loadData.run();
                    nameField.setText("");
                    priceField.setText("");
                    descField.setText("");
                    nameField.requestFocus();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "添加失败：" + ex.getMessage());
            }
        });

        // ====== 修改服务 ======
        updateBtn.addActionListener(evt -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "⚠️ 请先选择要修改的服务！");
                return;
            }

            int serviceId = (int) model.getValueAt(row, 0);
            String name = nameField.getText().trim();
            String priceStr = priceField.getText().trim();
            String type = (String) typeCombo.getSelectedItem();
            String desc = descField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ 服务名称不能为空！");
                nameField.requestFocus();
                return;
            }
            if (priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ 服务价格不能为空！");
                priceField.requestFocus();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    JOptionPane.showMessageDialog(this, "⚠️ 价格必须大于0！");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "⚠️ 价格必须是数字！");
                priceField.requestFocus();
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "确认修改服务 \"" + name + "\" ？", "确认修改",
                    JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            try {
                // 直接更新数据库
                String sql = "UPDATE service_item SET name=?, type=?, price=?, description=? WHERE id=? AND shop_id=?";
                java.sql.Connection conn = DBUtil.getConnection();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, name);
                pstmt.setString(2, type);
                pstmt.setDouble(3, price);
                pstmt.setString(4, desc);
                pstmt.setInt(5, serviceId);
                pstmt.setInt(6, currentShop.getId());
                int rows = pstmt.executeUpdate();
                pstmt.close();
                conn.close();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "✅ 服务修改成功！");
                    loadData.run();
                    nameField.setText("");
                    priceField.setText("");
                    descField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "修改失败，请检查权限！");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "修改失败：" + ex.getMessage());
            }
        });

        // ====== 删除服务 ======
        deleteBtn.addActionListener(evt -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "⚠️ 请先选择要删除的服务！");
                return;
            }

            int serviceId = (int) model.getValueAt(row, 0);
            String serviceName = (String) model.getValueAt(row, 1);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "⚠️ 确定要删除服务 \"" + serviceName + "\" 吗？\n此操作不可恢复！",
                    "确认删除",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;

            try {
                if (new ServiceItemDao().deleteService(serviceId)) {
                    JOptionPane.showMessageDialog(this, "✅ 服务删除成功！");
                    loadData.run();
                    nameField.setText("");
                    priceField.setText("");
                    descField.setText("");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
            }
        });

        // ====== 清空表单 ======
        clearBtn.addActionListener(evt -> {
            nameField.setText("");
            priceField.setText("");
            descField.setText("");
            nameField.requestFocus();
        });

        // ====== 点击表格行自动填充表单 ======
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    nameField.setText((String) model.getValueAt(row, 1));
                    String type = (String) model.getValueAt(row, 2);
                    typeCombo.setSelectedItem(type);
                    priceField.setText((String) model.getValueAt(row, 3));
                    descField.setText((String) model.getValueAt(row, 4));
                }
            }
        });

        refreshBtn.addActionListener(evt -> loadData.run());
        loadData.run();
        return panel;
    }

    // ==================== 预约管理面板 ====================
    private JPanel createAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (currentShop == null) {
            JLabel label = new JLabel("⚠️ 请先完善店铺信息！", JLabel.CENTER);
            label.setFont(new Font("微软雅黑", Font.BOLD, 16));
            label.setForeground(Color.RED);
            panel.add(label, BorderLayout.CENTER);
            return panel;
        }

        String[] columns = {"ID", "用户ID", "服务ID", "预约时间", "状态", "支付状态", "价格"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 12));
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        // 按钮面板
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton acceptBtn = new JButton("✅ 接单");
        acceptBtn.setBackground(new Color(46, 204, 113));
        acceptBtn.setForeground(Color.WHITE);
        acceptBtn.setToolTipText("接单后状态变为'已接单'");

        JButton completeBtn = new JButton("🎯 完成服务");
        completeBtn.setBackground(new Color(52, 152, 219));
        completeBtn.setForeground(Color.WHITE);
        completeBtn.setToolTipText("服务完成后状态变为'已完成'");

        JButton refreshBtn = new JButton("🔄 刷新");
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);

        btnPanel.add(acceptBtn);
        btnPanel.add(completeBtn);
        btnPanel.add(refreshBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // 加载数据
        Runnable loadData = () -> {
            model.setRowCount(0);
            try {
                List<Appointment> apps = new AppointmentDao().findAppointmentsByShopId(currentShop.getId());
                for (Appointment a : apps) {
                    String statusText = getStatusText(a.getStatus());
                    String payText = "paid".equals(a.getPayStatus()) ? "✅ 已支付" : "⏳ 未支付";
                    model.addRow(new Object[]{
                            a.getId(), a.getUserId(), a.getServiceId(),
                            sdf.format(a.getAppointmentTime()), statusText,
                            payText, String.format("%.2f", a.getPrice())
                    });
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
            }
        };

        acceptBtn.addActionListener(evt -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "⚠️ 请选择要处理的预约！");
                return;
            }
            int appId = (int) model.getValueAt(row, 0);
            String status = (String) model.getValueAt(row, 4);
            if (!"⏳ 待接单".equals(status)) {
                JOptionPane.showMessageDialog(this, "该预约不是待接单状态！");
                return;
            }
            try {
                if (new AppointmentDao().updateAppointmentStatus(appId, "accepted")) {
                    JOptionPane.showMessageDialog(this, "✅ 接单成功！");
                    loadData.run();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "操作失败：" + ex.getMessage());
            }
        });

        completeBtn.addActionListener(evt -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "⚠️ 请选择要处理的预约！");
                return;
            }
            int appId = (int) model.getValueAt(row, 0);
            String status = (String) model.getValueAt(row, 4);
            if (!"📋 已接单".equals(status)) {
                JOptionPane.showMessageDialog(this, "该预约不是已接单状态！");
                return;
            }
            try {
                if (new AppointmentDao().updateAppointmentStatus(appId, "completed")) {
                    JOptionPane.showMessageDialog(this, "✅ 服务完成！");
                    loadData.run();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "操作失败：" + ex.getMessage());
            }
        });

        refreshBtn.addActionListener(evt -> loadData.run());
        loadData.run();
        return panel;
    }

    // ==================== 评价查看面板 ====================
    private JPanel createEvaluationPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (currentShop == null) {
            JLabel label = new JLabel("⚠️ 请先完善店铺信息！", JLabel.CENTER);
            label.setFont(new Font("微软雅黑", Font.BOLD, 16));
            label.setForeground(Color.RED);
            panel.add(label, BorderLayout.CENTER);
            return panel;
        }

        String[] columns = {"ID", "用户ID", "内容", "评分", "时间"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 12));
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        // 统计信息
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel statsLabel = new JLabel("平均评分：--  |  评价总数：--");
        statsLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        bottomPanel.add(statsLabel, BorderLayout.WEST);

        JButton refreshBtn = new JButton("🔄 刷新");
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        bottomPanel.add(refreshBtn, BorderLayout.EAST);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        Runnable loadData = () -> {
            model.setRowCount(0);
            try {
                List<Evaluation> evals = new EvaluationDao().findEvaluationsByShopId(currentShop.getId());
                double sum = 0;
                for (Evaluation eval : evals) {
                    model.addRow(new Object[]{
                            eval.getId(), eval.getUserId(),
                            eval.getContent(), eval.getScore() + "⭐",
                            eval.getCreateTime() != null ? sdf.format(eval.getCreateTime()) : "-"
                    });
                    sum += eval.getScore();
                }
                if (!evals.isEmpty()) {
                    double avg = sum / evals.size();
                    statsLabel.setText("平均评分：" + String.format("%.1f", avg) + "⭐  |  评价总数：" + evals.size() + " 条");
                } else {
                    statsLabel.setText("暂无评价");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
            }
        };

        refreshBtn.addActionListener(evt -> loadData.run());
        loadData.run();
        return panel;
    }

    // ==================== 收入统计面板 ====================
    private JPanel createIncomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // 标题
        JLabel titleLabel = new JLabel("💰 收入统计");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        // 统计卡片
        JPanel cardPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        cardPanel.setMaximumSize(new Dimension(500, 200));

        JPanel card1 = createStatCard("总营收", "0.00 元", new Color(46, 204, 113));
        JPanel card2 = createStatCard("总预约数", "0 单", new Color(52, 152, 219));
        JPanel card3 = createStatCard("已完成订单", "0 单", new Color(155, 89, 182));
        JPanel card4 = createStatCard("待处理订单", "0 单", new Color(231, 76, 60));

        cardPanel.add(card1);
        cardPanel.add(card2);
        cardPanel.add(card3);
        cardPanel.add(card4);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.add(cardPanel);
        panel.add(wrapper);
        panel.add(Box.createVerticalStrut(20));

        // 计算按钮
        JButton calcBtn = new JButton("📊 计算收入统计");
        calcBtn.setBackground(new Color(52, 152, 219));
        calcBtn.setForeground(Color.WHITE);
        calcBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        calcBtn.setPreferredSize(new Dimension(180, 40));
        calcBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(calcBtn);

        // 保存卡片引用以便更新
        JLabel[] cardLabels = new JLabel[4];
        Component[] components = card1.getComponents();
        for (Component c : components) {
            if (c instanceof JLabel && ((JLabel) c).getText().contains("元")) {
                cardLabels[0] = (JLabel) c;
            }
        }
        components = card2.getComponents();
        for (Component c : components) {
            if (c instanceof JLabel && ((JLabel) c).getText().contains("单")) {
                cardLabels[1] = (JLabel) c;
            }
        }
        components = card3.getComponents();
        for (Component c : components) {
            if (c instanceof JLabel && ((JLabel) c).getText().contains("单")) {
                cardLabels[2] = (JLabel) c;
            }
        }
        components = card4.getComponents();
        for (Component c : components) {
            if (c instanceof JLabel && ((JLabel) c).getText().contains("单")) {
                cardLabels[3] = (JLabel) c;
            }
        }

        calcBtn.addActionListener(evt -> {
            if (currentShop == null) {
                JOptionPane.showMessageDialog(this, "请先完善店铺信息！");
                return;
            }
            try {
                List<Appointment> apps = new AppointmentDao().findAppointmentsByShopId(currentShop.getId());
                double total = 0;
                int totalCount = apps.size();
                int completedCount = 0;
                int pendingCount = 0;

                for (Appointment a : apps) {
                    if ("paid".equals(a.getPayStatus())) {
                        total += a.getPrice();
                    }
                    if ("completed".equals(a.getStatus())) {
                        completedCount++;
                    }
                    if ("pending".equals(a.getStatus())) {
                        pendingCount++;
                    }
                }

                cardLabels[0].setText(String.format("%.2f 元", total));
                cardLabels[1].setText(totalCount + " 单");
                cardLabels[2].setText(completedCount + " 单");
                cardLabels[3].setText(pendingCount + " 单");

                JOptionPane.showMessageDialog(this, "✅ 统计完成！");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "统计失败：" + ex.getMessage());
            }
        });

        return panel;
    }

    // 创建统计卡片
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        titleLabel.setForeground(Color.GRAY);
        panel.add(titleLabel, BorderLayout.NORTH);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }

    // ==================== 辅助方法 ====================
    private String getStatusText(String status) {
        switch (status) {
            case "pending": return "⏳ 待接单";
            case "accepted": return "📋 已接单";
            case "completed": return "✅ 已完成";
            case "cancelled": return "❌ 已取消";
            default: return status;
        }
    }

    // ==================== 主方法测试 ====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testUser = new User();
            testUser.setId(2);
            testUser.setNickname("宠物店老板");
            testUser.setRole("shop");
            new ShopMainFrame(testUser).setVisible(true);
        });
    }
}