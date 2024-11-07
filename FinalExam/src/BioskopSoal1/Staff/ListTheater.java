/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package BioskopSoal1.Staff;

import BioskopSoal1.Connection.DatabaseConnection;
import BioskopSoal1.LoginForm;
import BioskopSoal1.Util.UserSession;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ACER
 */
public class ListTheater extends javax.swing.JFrame {

    private int selectedID = -1;

    public ListTheater() {

        if (isSessionValid()) {
            setTitle("CinemaQ | Theater");

            initComponents();

            showData();

            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    exit();
                }
            });
        } else {
            this.dispose();
        }
    }

    private boolean isSessionValid() {
        if (!UserSession.isSessionValid()) {
            JOptionPane.showMessageDialog(this, "Harap Login Terlebih Dahulu", "Error", JOptionPane.ERROR_MESSAGE);

            SwingUtilities.invokeLater(() -> {
                new LoginForm().setVisible(true);
            });

            SwingUtilities.invokeLater(this::dispose);

            return false;
        }
        return true;
    }

    private void exit() {
        int response = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin keluar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        } else {
            return;
        }
    }

    private void showData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                String query = "SELECT * FROM Theater";
                PreparedStatement stmt = conn.prepareStatement(query);

                ResultSet rs = stmt.executeQuery();
                DefaultTableModel tblModel = new DefaultTableModel();
                tblModel.addColumn("ID");
                tblModel.addColumn("No.");
                tblModel.addColumn("Nama Theater");
                tblModel.addColumn("Deskripsi");

                tblModel.getDataVector().removeAllElements();
                tblModel.fireTableDataChanged();
                tblModel.setRowCount(0);

                int no = 1;
                while (rs.next()) {
                    Object[] data = new Object[]{
                        rs.getString("ID"),
                        no,
                        rs.getString("NAME"),
                        rs.getString("DESCRIPTION"),};
                    tblModel.addRow(data);
                    tblTheater.setModel(tblModel);
                    no++;
                }

                tblTheater.getColumnModel().getColumn(0).setMinWidth(0);
                tblTheater.getColumnModel().getColumn(0).setMaxWidth(0);
                tblTheater.getColumnModel().getColumn(0).setWidth(0);
                tblTheater.getColumnModel().getColumn(0).setResizable(true);
            }
        } catch (Exception ex) {
        }
    }

    private void search() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                String keyword = txtSearch.getText().toString();
                String query = "SELECT * FROM Theater WHERE name LIKE ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, "%" + keyword + "%");

                ResultSet rs = stmt.executeQuery();
                DefaultTableModel tblModel = new DefaultTableModel();
                tblModel.addColumn("ID");
                tblModel.addColumn("No.");
                tblModel.addColumn("Nama Theater");
                tblModel.addColumn("Deskripsi");

                tblModel.getDataVector().removeAllElements();
                tblModel.fireTableDataChanged();
                tblModel.setRowCount(0);

                int no = 1;
                boolean dataFound = false;
                while (rs.next()) {
                    dataFound = true;
                    Object[] data = new Object[]{
                        rs.getString("ID"),
                        no,
                        rs.getString("NAME"),
                        rs.getString("DESCRIPTION"),};
                    tblModel.addRow(data);
                    tblTheater.setModel(tblModel);
                    no++;
                }

                tblTheater.getColumnModel().getColumn(0).setMinWidth(0);
                tblTheater.getColumnModel().getColumn(0).setMaxWidth(0);
                tblTheater.getColumnModel().getColumn(0).setWidth(0);
                tblTheater.getColumnModel().getColumn(0).setResizable(true);
                
                if (!dataFound) {
                    tblModel.setRowCount(0);
                    tblTheater.setModel(tblModel);
                }

            }
        } catch (Exception ex) {
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTheater = new javax.swing.JTable();
        btnTambahData = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuDashboard = new javax.swing.JMenu();
        menuTheater = new javax.swing.JMenu();
        menuFilm = new javax.swing.JMenu();
        menuJadwalTayang = new javax.swing.JMenu();
        menuPenjualanTiket = new javax.swing.JMenu();
        menuLogout = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Data Theater");

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
        });

        tblTheater.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "No", "Nama Theater", "Deskripsi"
            }
        ));
        tblTheater.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTheaterMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblTheater);

        btnTambahData.setBackground(new java.awt.Color(54, 92, 245));
        btnTambahData.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTambahData.setForeground(new java.awt.Color(255, 255, 255));
        btnTambahData.setText("Tambah Data");
        btnTambahData.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(54, 92, 245), 2));
        btnTambahData.setBorderPainted(false);
        btnTambahData.setPreferredSize(new java.awt.Dimension(50, 35));
        btnTambahData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahDataActionPerformed(evt);
            }
        });

        btnEdit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(54, 92, 245));
        btnEdit.setText("Edit Data");
        btnEdit.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(54, 92, 245), 2, true));
        btnEdit.setContentAreaFilled(false);
        btnEdit.setPreferredSize(new java.awt.Dimension(50, 35));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnHapus.setBackground(new java.awt.Color(0, 0, 0));
        btnHapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus Data");
        btnHapus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        btnHapus.setBorderPainted(false);
        btnHapus.setPreferredSize(new java.awt.Dimension(75, 35));
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        menuDashboard.setText("Dashboard");
        menuDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuDashboardMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuDashboard);

        menuTheater.setText("Theater");
        menuTheater.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuTheaterMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuTheater);

        menuFilm.setText("Film");
        menuFilm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuFilmMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuFilm);

        menuJadwalTayang.setText("Jadwal Tayang");
        menuJadwalTayang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuJadwalTayangMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuJadwalTayang);

        menuPenjualanTiket.setText("Penjualan Tiket");
        menuPenjualanTiket.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuPenjualanTiketMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuPenjualanTiket);

        menuLogout.setText("Logout");
        menuLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuLogoutMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuLogout);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(137, 137, 137)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTambahData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTambahData, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(95, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuLogoutMouseClicked
        int response = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            UserSession.clearSession();
            new LoginForm().setVisible(true);
            this.dispose();
        } else {
            return;
        }
    }//GEN-LAST:event_menuLogoutMouseClicked

    private void menuDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuDashboardMouseClicked
        new StaffDashboard().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuDashboardMouseClicked

    private void menuFilmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuFilmMouseClicked
        new ListFilm().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuFilmMouseClicked

    private void menuJadwalTayangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuJadwalTayangMouseClicked
        new ListJadwalTayang().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuJadwalTayangMouseClicked

    private void menuPenjualanTiketMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuPenjualanTiketMouseClicked
        new PenjualanTiket().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuPenjualanTiketMouseClicked

    private void btnTambahDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahDataActionPerformed
        new FormTheater(-1).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnTambahDataActionPerformed

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        search();
    }//GEN-LAST:event_txtSearchKeyPressed

    private void tblTheaterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTheaterMouseClicked
        int selectedRow = tblTheater.getSelectedRow();
        if (selectedRow != -1) {
            selectedID = Integer.valueOf(tblTheater.getValueAt(selectedRow, 0).toString());
            System.out.println("ID yang dipilih: " + selectedID);
        }
    }//GEN-LAST:event_tblTheaterMouseClicked

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (selectedID != -1) {
            new FormTheater(selectedID).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Mohon pilih data terlebih dahulu", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if (selectedID != -1) {
            int konfirmasi = JOptionPane.showConfirmDialog(this, "Apakah anda yakin menghapus data ini ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (konfirmasi == JOptionPane.YES_OPTION) {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    if (conn != null) {
                        String query = "DELETE FROM THEATER WHERE ID = '" + selectedID + "'";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.executeUpdate();

                        JOptionPane.showMessageDialog(this, "Hapus data berhasil");
                        showData();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Mohon pilih data terlebih dahulu", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void menuTheaterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuTheaterMouseClicked
        new ListTheater().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuTheaterMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ListTheater.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListTheater.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListTheater.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListTheater.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ListTheater().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnTambahData;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenu menuDashboard;
    private javax.swing.JMenu menuFilm;
    private javax.swing.JMenu menuJadwalTayang;
    private javax.swing.JMenu menuLogout;
    private javax.swing.JMenu menuPenjualanTiket;
    private javax.swing.JMenu menuTheater;
    private javax.swing.JTable tblTheater;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
