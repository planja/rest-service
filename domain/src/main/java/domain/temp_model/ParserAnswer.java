package domain.temp_model;

import javax.persistence.*;

@Entity
@Table(name = "parser_answers")
public class ParserAnswer {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @Column(name = "AA")
    private int AA;

    @Column(name = "AС")
    private int AC;

    @Column(name = "AF")
    private int AF;

    @Column(name = "AS")
    private int AS;

    @Column(name = "BA")
    private int BA;

    @Column(name = "CX")
    private int CX;

    @Column(name = "DL")
    private int DL;

    @Column(name = "EK")
    private int EK;

    @Column(name = "EY")
    private int EY;

    @Column(name = "JL")
    private int JL;

    @Column(name = "LH")
    private int LH;

    @Column(name = "NH")
    private int NH;

    @Column(name = "QF")
    private int QF;

    @Column(name = "QR")
    private int QR;

    @Column(name = "SQ")
    private int SQ;

    @Column(name = "VS")
    private int VS;

    @Column(name = "UA")
    private int UA;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public int getAA() {
        return AA;
    }

    public void setAA(int AA) {
        this.AA = AA;
    }

    public int getAC() {
        return AC;
    }

    public void setAC(int AC) {
        this.AC = AC;
    }

    public int getAF() {
        return AF;
    }

    public void setAF(int AF) {
        this.AF = AF;
    }

    public int getAS() {
        return AS;
    }

    public void setAS(int AS) {
        this.AS = AS;
    }

    public int getBA() {
        return BA;
    }

    public void setBA(int BA) {
        this.BA = BA;
    }

    public int getCX() {
        return CX;
    }

    public void setCX(int CX) {
        this.CX = CX;
    }

    public int getDL() {
        return DL;
    }

    public void setDL(int DL) {
        this.DL = DL;
    }

    public int getEK() {
        return EK;
    }

    public void setEK(int EK) {
        this.EK = EK;
    }

    public int getEY() {
        return EY;
    }

    public void setEY(int EY) {
        this.EY = EY;
    }

    public int getJL() {
        return JL;
    }

    public void setJL(int JL) {
        this.JL = JL;
    }

    public int getLH() {
        return LH;
    }

    public void setLH(int LH) {
        this.LH = LH;
    }

    public int getNH() {
        return NH;
    }

    public void setNH(int NH) {
        this.NH = NH;
    }

    public int getQF() {
        return QF;
    }

    public void setQF(int QF) {
        this.QF = QF;
    }

    public int getQR() {
        return QR;
    }

    public void setQR(int QR) {
        this.QR = QR;
    }

    public int getSQ() {
        return SQ;
    }

    public void setSQ(int SQ) {
        this.SQ = SQ;
    }

    public int getVS() {
        return VS;
    }

    public void setVS(int VS) {
        this.VS = VS;
    }

    public int getUA() {
        return UA;
    }

    public void setUA(int UA) {
        this.UA = UA;
    }
}
