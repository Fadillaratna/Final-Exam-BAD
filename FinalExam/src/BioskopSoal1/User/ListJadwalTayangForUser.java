/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package BioskopSoal1.User;

import BioskopSoal1.Staff.*;
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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ACER
 */
public class ListJadwalTayangForUser extends javax.swing.JFrame {

    private int selectedID = -1;

    public ListJadwalTayangForUser() {

        if (isSessionValid()) {
            setTitle("CinemaQ | Jadwal Tayang");

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
                    SELECT fs.id AS schedule_id,
                           m.title AS movie_title,
                           m.duration,
                           m.description,
                           m.genre,
                           t.name AS theater_name,
                           fs.schedule_date,
                           fs.price,
                           (SELECT COUNT(id)
                            FROM seat
                            WHERE film_schedule_id = fs.id
                            AND is_available = true) AS available_seat_count
                    FROM film_schedule fs
                    INNER JOIN movies m ON fs.movie_id = m.id
                    INNER JOIN theater t ON fs.theater_id = t.id
                    WHERE (DATE(fs.schedule_date) = CURRENT_DATE AND fs.schedule_date > NOW())
                    ORDER BY fs.id DESC;
                """;

                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                DefaultTableModel tblModel = new DefaultTableModel();
                tblModel.addColumn("ID");
                tblModel.addColumn("No.");
                tblModel.addColumn("Judul Film");
                tblModel.addColumn("Durasi");
                tblModel.addColumn("Deskripsi");
                tblModel.addColumn("Genre");
                tblModel.addColumn("Tanggal Tayang");
                tblModel.addColumn("Jam Tayang");
                tblModel.addColumn("Theater");
                tblModel.addColumn("Harga");
                tblModel.addColumn("Kursi Available");

                tblModel.getDataVector().removeAllElements();
                tblModel.fireTableDataChanged();
                tblModel.setRowCount(0);

                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                int no = 1;
                boolean isFound = false;
                while (rs.next()) {
                    isFound = true;
                    String showDate = "";
                    String showTime = "";
                    try {
                        Date date = rs.getTimestamp("schedule_date");
                        showDate = dateFormat.format(date);
                        showTime = timeFormat.format(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Object[] data = new Object[]{
                        rs.getString("schedule_id"),
                        no,
                        rs.getString("movie_title"),
                        rs.getString("duration") + " menit",
                        rs.getString("description"),
                        rs.getString("genre"),
                        showDate,
                        showTime,
                        rs.getString("theater_name"),
                        "Rp" + rs.getDouble("price"),
                        rs.getInt("available_seat_count")
                    };
                    tblModel.addRow(data);
                    tblJadwal.setModel(tblModel);
                    no++;
                }

                if (!isFound) {
                    tblModel.setRowCount(0);
                    tblJadwal.setModel(tblModel);
                }
                tblJadwal.getColumnModel().getColumn(0).setMinWidth(0);
                tblJadwal.getColumnModel().getColumn(0).setMaxWidth(0);
                tblJadwal.getColumnModel().getColumn(0).setWidth(0);
                tblJadwal.getColumnModel().getColumn(0).setResizable(true);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void search() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                String where = "";
                String keyword = txtJudulFilm.getText();

                String query = """
                    SELECT fs.id AS schedule_id,
                           m.title AS movie_title,
                           m.duration,
                           m.description,
                           m.genre,
                           t.name AS theater_name,
                           fs.schedule_date,
                           fs.price,
                           (SELECT COUNT(id)
                            FROM seat
                            WHERE film_schedule_id = fs.id
                            AND is_available = true) AS available_seat_count
                    FROM film_schedule fs
                    INNER JOIN movies m ON fs.movie_id = m.id
                    INNER JOIN theater t ON fs.theater_id = t.id
                    WHERE (DATE(fs.schedule_date) = CURRENT_DATE AND fs.schedule_date > NOW()) AND m.title Like ? 
                """ + " " + where + " ORDER BY fs.schedule_date DESC";

                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, "%" + keyword + "%");
                ResultSet rs = stmt.executeQuery();

                DefaultTableModel tblModel = new DefaultTableModel();
                tblModel.addColumn("ID");
                tblModel.addColumn("No.");
                tblModel.addColumn("Judul Film");
                tblModel.addColumn("Durasi");
                tblModel.addColumn("Deskripsi");
                tblModel.addColumn("Genre");
                tblModel.addColumn("Tanggal Tayang");
                tblModel.addColumn("Jam Tayang");
                tblModel.addColumn("Theater");
                tblModel.addColumn("Harga");
                tblModel.addColumn("Kursi Available");

                tblModel.getDataVector().removeAllElements();
                tblModel.fireTableDataChanged();
                tblModel.setRowCount(0);

                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                int no = 1;
                boolean dataFound = false;
                while (rs.next()) {
                    dataFound = true;
                    String scheduleDate = rs.getString("schedule_date");
                    String showDate = "";
                    String showTime = "";
                    try {
                        Date date = rs.getTimestamp("schedule_date");
                        showDate = dateFormat.format(date);
                        showTime = timeFormat.format(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Object[] data = new Object[]{
                        rs.getString("schedule_id"),
                        no,
                        rs.getString("movie_title"),
                        rs.getString("description"),
                        rs.getString("genre"),
                        rs.getString("duration") + " menit",
                        showDate,
                        showTime,
                        rs.getString("theater_name"),
                        "Rp" + rs.getDouble("price"),
                        rs.getInt("available_seat_count")
                    };
                    tblModel.addRow(data);
                    tblJadwal.setModel(tblModel);
                    no++;
                }

                if (!dataFound) {
                    tblModel.setRowCount(0);
                    tblJadwal.setModel(tblModel);
                }

                tblJadwal.getColumnModel().getColumn(0).setMinWidth(0);
                tblJadwal.getColumnModel().getColumn(0).setMaxWidth(0);
                tblJadwal.getColumnModel().getColumn(0).setWidth(0);
                tblJadwal.getColumnModel().getColumn(0).setResizable(true);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtJudulFilm = new javax.swing.JTextField();
        btnBeli = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblJadwal = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuDashboard = new javax.swing.JMenu();
        menuJadwalTayang = new javax.swing.JMenu();
        menuHistori = new javax.swing.JMenu();
        menuLogout = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Film Hari Ini");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Judul Film");

        txtJudulFilm.setPreferredSize(new java.awt.Dimension(330, 22));
        txtJudulFilm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtJudulFilmKeyPressed(evt);
            }
        });

        btnBeli.setBackground(new java.awt.Color(54, 92, 245));
        btnBeli.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBeli.setForeground(new java.awt.Color(255, 255, 255));
        btnBeli.setText("Beli");
        btnBeli.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(54, 92, 245), 2));
        btnBeli.setBorderPainted(false);
        btnBeli.setPreferredSize(new java.awt.Dimension(109, 35));
        btnBeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBeliActionPerformed(evt);
            }
        });

        tblJadwal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Judul Film", "Durasi", "Deskripsi", "Genre", "Tanggal Tayang", "Jam Tayang", "Theater", "Harga", "Kursi Available"
            }
        ));
        tblJadwal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblJadwalMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblJadwal);
        if (tblJadwal.getColumnModel().getColumnCount() > 0) {
            tblJadwal.getColumnModel().getColumn(9).setResizable(false);
        }

        menuDashboard.setText("Dashboard");
        menuDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuDashboardMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuDashboard);

        menuJadwalTayang.setText("Film Hari Ini");
        menuJadwalTayang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuJadwalTayangMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuJadwalTayang);

        menuHistori.setText("Histori Pembelian");
        menuHistori.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuHistoriMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuHistori);

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
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtJudulFilm, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1265, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(36, 36, 36))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtJudulFilm, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(200, 200, 200))
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
        new UserDashboard().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuDashboardMouseClicked

    private void menuJadwalTayangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuJadwalTayangMouseClicked
        new ListJadwalTayangForUser().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuJadwalTayangMouseClicked

    private void menuHistoriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuHistoriMouseClicked
        new HistoriPembelian().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuHistoriMouseClicked

    private void btnBeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBeliActionPerformed
        if (selectedID != -1) {
            new PembelianTiket(selectedID).setVisible(true);
            this.dispose();
        }else{
            JOptionPane.showMessageDialog(this, "Mohon pilih data terlebih dahulu", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

    }//GEN-LAST:event_btnBeliActionPerformed

    private void txtJudulFilmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJudulFilmKeyPressed
        search();
    }//GEN-LAST:event_txtJudulFilmKeyPressed

    private void tblJadwalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblJadwalMouseClicked
        int selectedRow = tblJadwal.getSelectedRow();
        if (selectedRow != -1) {
            selectedID = Integer.valueOf(tblJadwal.getValueAt(selectedRow, 0).toString());
        }
    }//GEN-LAST:event_tblJadwalMouseClicked

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
            java.util.logging.Logger.getLogger(ListJadwalTayangForUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListJadwalTayangForUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListJadwalTayangForUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListJadwalTayangForUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ListJadwalTayangForUser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBeli;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenu menuDashboard;
    private javax.swing.JMenu menuHistori;
    private javax.swing.JMenu menuJadwalTayang;
    private javax.swing.JMenu menuLogout;
    private javax.swing.JTable tblJadwal;
    private javax.swing.JTextField txtJudulFilm;
    // End of variables declaration//GEN-END:variables
}
