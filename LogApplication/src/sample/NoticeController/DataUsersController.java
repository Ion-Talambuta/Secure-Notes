package sample.NoticeController;

import java.sql.Date;

public class DataUsersController {
    String idUser;
    String idusers_notes;
    Date data;
    String notes;

    /**
     * -->Notice parameters set to be taken over and used in the required processes:
     * @param iduser Notice id table
     * @param date Local Date for notice
     * @param notes Notice of users
     */
    public DataUsersController(String iduser, String idNotes, Date date, String notes){
        this.idUser = iduser;
        this.idusers_notes = idNotes;
        this.data = date;
        this.notes = notes;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdusers_notes() {
        return idusers_notes;
    }

    public void setIdusers_notes(String idusers_notes) {
        this.idusers_notes = idusers_notes;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
