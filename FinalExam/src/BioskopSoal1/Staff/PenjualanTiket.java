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
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ACER
 */
public class PenjualanTiket extends javax.swing.JFrame {

    public PenjualanTiket() {

        if (isSessionValid()) {
            setTitle("CinemaQ | Penjualan Tiket");

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
                String query = """
                   SELECT 
                        s.sale_date AS tgl_pembelian,
                        u.name as nama_cust,
                        f.title AS judul_film,
                        f.duration AS durasi,
                        fs.schedule_date AS jadwal_tayang,
                        t.name AS gedung_theater,
                        s.total_amount AS total_harga,
                        GROUP_CONCAT(CONCAT(seat.row, '-', seat.col) ORDER BY seat.row, seat.col) AS nomor_kursi
                    FROM 
                        sales s
                    JOIN 
                        sales_detail sd ON s.id = sd.sale_id
                    JOIN 
                        seat seat ON sd.seat_id = seat.id
                    JOIN 
                        film_schedule fs ON s.schedule_id = fs.id
                    JOIN 
                        movies f ON fs.movie_id = f.id
                    JOIN 
                        theater t ON fs.theater_id = t.id
                    JOIN
                        users u on u.ID = s.customer_id
                    GROUP BY 
                        s.id, f.title, fs.schedule_date, t.name
                    ORDER BY 
                        s.sale_date DESC;
                """;

                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                DefaultTableModel tblModel = new DefaultTableModel();
                tblModel.addColumn("No.");
                tblModel.addColumn("Tanggal Pembelian");
                tblModel.addColumn("Nama Customer");
                tblModel.addColumn("Judul Film");
                tblModel.addColumn("Durasi");
                tblModel.addColumn("Theater");
                tblModel.addColumn("Jadwal Tayang");
                tblModel.addColumn("Nomor Kursi");
                tblModel.addColumn("Total Harga");

                tblModel.getDataVector().removeAllElements();
                tblModel.fireTableDataChanged();
                tblModel.setRowCount(0);

                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm");

                int no = 1;
                boolean isFound = false;
                while (rs.next()) {
                    isFound = true;
                    String jadwalTayang = "";
                    String tanggalBeli = "";
                    try {
                        Date date = rs.getTimestamp("jadwal_tayang");
                        jadwalTayang = dateFormat.format(date);
                        date = rs.getTimestamp("tgl_pembelian");
                        tanggalBeli = dateFormat.format(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Object[] data = new Object[]{
                        no,
                        tanggalBeli,
                        rs.getString("nama_cust"),
                        rs.getString("judul_film"),
                        rs.getString("durasi") + " menit",
                        rs.getString("gedung_theater"),
                        jadwalTayang,
                        rs.getString("nomor_kursi"),
                        "Rp" + rs.getDouble("total_harga")
                    };
                    tblModel.addRow(data);
                    tblHistori.setModel(tblModel);
                    no++;
                }

                if (!isFound) {
                    tblModel.setRowCount(0);
                    tblHistori.setModel(tblModel);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHistori = new javax.swing.JTable();
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
        jLabel1.setText("Penjualan Tiket");

        tblHistori.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Tanggal Pembelian", "Nama Customer", "Judul Film", "Durasi", "Theater", "Jadwal Tayang", "Nomor Kursi", "Total Harga"
            }
        ));
        jScrollPane1.setViewportView(tblHistori);

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
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1124, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addGap(23, 23, 23))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel1)
                .addGap(32, 32, 32)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(PenjualanTiket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PenjualanTiket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PenjualanTiket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PenjualanTiket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PenjualanTiket().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenu menuDashboard;
    private javax.swing.JMenu menuFilm;
    private javax.swing.JMenu menuJadwalTayang;
    private javax.swing.JMenu menuLogout;
    private javax.swing.JMenu menuPenjualanTiket;
    private javax.swing.JMenu menuTheater;
    private javax.swing.JTable tblHistori;
    // End of variables declaration//GEN-END:variables
}
