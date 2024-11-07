package BioskopSoal1.Staff;

import BioskopSoal1.Connection.DatabaseConnection;
import BioskopSoal1.LoginForm;
import BioskopSoal1.Util.UserSession;
import com.mysql.jdbc.Statement;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.naming.directory.SearchResult;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class FormJadwalTayang extends javax.swing.JFrame {

    public FormJadwalTayang() {
        if (isSessionValid()) {
            setTitle("CinemaQ | Form Theater");

            initComponents();
            loadFilms();
            inputJamTayang.setModel(new SpinnerDateModel());
            JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(inputJamTayang, "HH:mm:ss");
            inputJamTayang.setEditor(timeEditor);

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

    private void loadFilms() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT id, title FROM movies";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                comboboxFilm.addItem(id + " - " + title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkAvailability() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                String where = "";
                String dlimWhere = "";

                DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
                comboModel.removeAllElements();

                String judulFilm = (String) comboboxFilm.getSelectedItem();
                if (judulFilm != null && !judulFilm.isEmpty()) {
                    String[] parts = judulFilm.split(" - ");
                    String filmId = parts[0];

                    where = "m.title LIKE ?";
                    dlimWhere = " AND ";

                    if (inputTglTayang.getDate() != null && inputJamTayang.getValue() != null) {
                        String tglTayang = new SimpleDateFormat("yyyy-MM-dd").format(inputTglTayang.getDate());
                        String jamTayang = new SimpleDateFormat("HH:mm:ss").format(inputJamTayang.getValue());

                        String startTime = tglTayang + " " + jamTayang;

                        String queryDuration = "SELECT duration FROM movies WHERE id = ?";
                        try (PreparedStatement stmtDur = conn.prepareStatement(queryDuration)) {
                            stmtDur.setString(1, filmId);
                            ResultSet rsDur = stmtDur.executeQuery();
                            int movieDuration = 0;

                            if (rsDur.next()) {
                                movieDuration = rsDur.getInt("duration");
                            }

                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            calendar.setTime(sdf.parse(startTime));
                            calendar.add(Calendar.MINUTE, movieDuration);
                            String endTime = sdf.format(calendar.getTime());

                            String query = """
                                SELECT DISTINCT t.id, t.name
                                FROM theater t
                                WHERE NOT EXISTS (
                                    SELECT 1
                                    FROM film_schedule fs
                                    INNER JOIN movies m ON m.id = fs.movie_id
                                    WHERE fs.theater_id = t.id
                                    AND (
                                        -- Cek apakah jadwal film yang sudah ada bertabrakan dengan jadwal baru
                                        (fs.schedule_date >= ? AND fs.schedule_date < ?)
                                        OR
                                        (fs.schedule_date + INTERVAL m.duration MINUTE > ? AND fs.schedule_date < ?)
                                    )
                                )
                                """;

                            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                                stmt.setString(1, startTime);
                                stmt.setString(2, endTime);
                                stmt.setString(3, startTime);
                                stmt.setString(4, endTime);

                                ResultSet rs = stmt.executeQuery();

                                while (rs.next()) {
                                    comboModel.addElement(rs.getString("id") + " - " + rs.getString("name"));
                                }
                            }
                        }
                    }

                }
                comboboxTheater.setModel(comboModel);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnSimpan = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        comboboxFilm = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        inputTglTayang = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        inputJamTayang = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        comboboxTheater = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtHarga = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtBaris = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtKolom = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Data Jadwal Tayang");

        btnSimpan.setBackground(new java.awt.Color(54, 92, 245));
        btnSimpan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("Simpan");
        btnSimpan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(54, 92, 245), 2));
        btnSimpan.setBorderPainted(false);
        btnSimpan.setPreferredSize(new java.awt.Dimension(109, 23));
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnBack.setBackground(new java.awt.Color(54, 92, 245));
        btnBack.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBack.setForeground(new java.awt.Color(54, 92, 245));
        btnBack.setText("Kembali");
        btnBack.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(54, 92, 245), 2));
        btnBack.setContentAreaFilled(false);
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Film");

        comboboxFilm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboboxFilmActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Tanggal Tayang");

        inputTglTayang.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                inputTglTayangPropertyChange(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Jam Tayang");

        inputJamTayang.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                inputJamTayangStateChanged(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Theater");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Harga");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Jumlah Kursi (Baris)");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Jumlah Kursi (Kolom)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(txtBaris, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE))
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtKolom, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8)))
                            .addComponent(comboboxFilm, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(inputTglTayang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(inputJamTayang, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboboxTheater, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtHarga, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(29, 29, 29))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel1)
                .addGap(40, 40, 40)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(comboboxFilm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(inputTglTayang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(inputJamTayang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(comboboxTheater, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBaris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKolom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if (comboboxFilm.getSelectedItem() == null || inputTglTayang.getDate() == null
                || inputJamTayang.getValue() == null || comboboxTheater.getSelectedItem() == null
                || txtHarga.getText().isEmpty() || txtBaris.getText().isEmpty() || txtKolom.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data tidak boleh kosong!", "Validasi Data", JOptionPane.ERROR_MESSAGE);
            return;
        }

        float harga;
        try {
            harga = Float.parseFloat(txtHarga.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka", "Validasi Data", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int baris;
        try {
            baris = Integer.parseInt(txtBaris.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah baris kursi harus berupa angka", "Validasi Data", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int kolom;
        try {
            kolom = Integer.parseInt(txtKolom.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah kolom kursi harus berupa angka", "Validasi Data", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tglTayang = new SimpleDateFormat("yyyy-MM-dd").format(inputTglTayang.getDate()) + " " + new SimpleDateFormat("HH:mm:ss").format(inputJamTayang.getValue());
        String selectedMovie = (String) comboboxFilm.getSelectedItem();
        int idMovie = Integer.valueOf(selectedMovie.split(" - ")[0]);
        String selectedTheater = (String) comboboxTheater.getSelectedItem();
        int idTheater = Integer.valueOf(selectedTheater.split(" - ")[0]);

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                String insertSchedule = "INSERT INTO film_schedule (theater_id, movie_id, schedule_date, price) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertSchedule, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, idTheater);
                    ps.setInt(2, idMovie);
                    ps.setString(3, tglTayang);
                    ps.setDouble(4, harga);
                    ps.executeUpdate();

                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        int filmScheduleId = rs.getInt(1);
                        String insertSeat = "INSERT INTO seat (row, col, is_available, theater_id, film_schedule_id) VALUES (?, ?, true, ?, ?)";
                        try (PreparedStatement psSeat = conn.prepareStatement(insertSeat)) {
                            for (int row = 1; row <= baris; row++) {
                                for (int col = 1; col <= kolom; col++) {
                                    String rowLetter = String.valueOf((char) ('A' + row - 1));

                                    psSeat.setString(1, rowLetter);
                                    psSeat.setInt(2, col);
                                    psSeat.setInt(3, idTheater);
                                    psSeat.setInt(4, filmScheduleId);
                                    psSeat.addBatch();
                                }
                            }
                            psSeat.executeBatch();
                        }
                    }
                }
                JOptionPane.showMessageDialog(this, "Sukses Menambah Data!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                new ListJadwalTayang().setVisible(true);
                this.dispose();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        new ListJadwalTayang().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void inputJamTayangStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_inputJamTayangStateChanged
        checkAvailability();
    }//GEN-LAST:event_inputJamTayangStateChanged

    private void inputTglTayangPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_inputTglTayangPropertyChange
        checkAvailability();
    }//GEN-LAST:event_inputTglTayangPropertyChange

    private void comboboxFilmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboboxFilmActionPerformed
        checkAvailability();
    }//GEN-LAST:event_comboboxFilmActionPerformed

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
            java.util.logging.Logger.getLogger(FormJadwalTayang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormJadwalTayang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormJadwalTayang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormJadwalTayang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormJadwalTayang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> comboboxFilm;
    private javax.swing.JComboBox<String> comboboxTheater;
    private javax.swing.JSpinner inputJamTayang;
    private com.toedter.calendar.JDateChooser inputTglTayang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField txtBaris;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtKolom;
    // End of variables declaration//GEN-END:variables
}
