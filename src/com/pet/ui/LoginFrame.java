package com.pet.ui;

import com.pet.dao.UserDao;
import com.pet.entity.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheck;

    public LoginFrame() {
        setTitle("🐾 宠物服务预约系统 - 登录");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Logo标题
        JLabel titleLabel = new JLabel("🐕 宠物服务预约系统", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(new Color(52, 152, 219));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 手机号
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("📱 手机号："), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        phoneField = new JTextField(15);
        phoneField.setToolTipText("请输入注册手机号");
        formPanel.add(phoneField, gbc);

        // 密码
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("🔒 密码："), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        passwordField = new JPasswordField(15);
        passwordField.setToolTipText("请输入密码");
        formPanel.add(passwordField, gbc);

        // 显示密码
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        showPasswordCheck = new JCheckBox("显示密码");
        showPasswordCheck.addActionListener(e -> {
            if (showPasswordCheck.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        });
        formPanel.add(showPasswordCheck, gbc);

        // 按钮面板
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        JButton loginBtn = new JButton("登录");
        loginBtn.setBackground(new Color(52, 152, 219));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        loginBtn.setPreferredSize(new Dimension(90, 35));
        loginBtn.addActionListener(e -> login());

        JButton registerBtn = new JButton("注册");
        registerBtn.setBackground(new Color(46, 204, 113));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setPreferredSize(new Dimension(90, 35));
        registerBtn.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });

        JButton forgotBtn = new JButton("忘记密码");
        forgotBtn.setForeground(new Color(231, 76, 60));
        forgotBtn.setBorderPainted(false);
        forgotBtn.setContentAreaFilled(false);
        forgotBtn.addActionListener(e -> showForgotPasswordDialog());

        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);
        btnPanel.add(forgotBtn);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        formPanel.add(btnPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // 底部提示
        JLabel tipLabel = new JLabel("默认账号: admin/admin, user/123456, shopowner/123456", JLabel.CENTER);
        tipLabel.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        tipLabel.setForeground(Color.GRAY);
        mainPanel.add(tipLabel, BorderLayout.SOUTH);

        add(mainPanel);

        // 按Enter登录
        getRootPane().setDefaultButton(loginBtn);
    }

    private void login() {
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ 请输入手机号！", "提示", JOptionPane.WARNING_MESSAGE);
            phoneField.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ 请输入密码！", "提示", JOptionPane.WARNING_MESSAGE);
            passwordField.requestFocus();
            return;
        }

        try {
            UserDao userDao = new UserDao();
            User user = userDao.login(phone, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "✅ 登录成功！欢迎您，" + user.getNickname());
                openMainFrame(user);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ 手机号或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                passwordField.requestFocus();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ 登录失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openMainFrame(User user) {
        switch (user.getRole()) {
            case "admin":
                new AdminMainFrame(user).setVisible(true);
                break;
            case "shop":
                new ShopMainFrame(user).setVisible(true);
                break;
            case "user":
            case "vip":
                new UserMainFrame(user).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "未知角色！");
        }
    }

    private void showForgotPasswordDialog() {
        JDialog dialog = new JDialog(this, "找回密码", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("请输入注册手机号："), gbc);

        gbc.gridx = 1;
        JTextField phoneField = new JTextField(12);
        panel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JButton resetBtn = new JButton("重置密码为 123456");
        resetBtn.addActionListener(e -> {
            String phone = phoneField.getText().trim();
            if (phone.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "请输入手机号！");
                return;
            }
            try {
                UserDao dao = new UserDao();
                if (dao.isPhoneExists(phone)) {
                    // 查找用户并重置密码
                    User user = dao.login(phone, "123456");
                    if (user == null) {
                        // 如果123456不对，直接更新
                        user = dao.findUserById(dao.findAllUsers().stream()
                                .filter(u -> u.getPhone().equals(phone))
                                .findFirst().get().getId());
                        user.setPassword("123456");
                        dao.updateUser(user);
                    }
                    JOptionPane.showMessageDialog(dialog, "✅ 密码已重置为：123456");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "❌ 该手机号未注册！");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "操作失败：" + ex.getMessage());
            }
        });
        panel.add(resetBtn, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}