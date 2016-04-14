package domain.model;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Objects;

/**
 * Created by Никита on 12.04.2016.
 */

@Entity
@Table(name = "parser_answers")
public class ParserAnswer {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @Column(name = "AA")
    private Integer AA;

    @Column(name = "AС")
    private Integer AC;

    @Column(name = "AF")
    private Integer AF;

    @Column(name = "AS")
    private Integer AS;

    @Column(name = "BA")
    private Integer BA;

    @Column(name = "CX")
    private Integer CX;

    @Column(name = "DL")
    private Integer DL;

    @Column(name = "EK")
    private Integer EK;

    @Column(name = "EY")
    private Integer EY;

    @Column(name = "JL")
    private Integer JL;

    @Column(name = "LH")
    private Integer LH;

    @Column(name = "NH")
    private Integer NH;

    @Column(name = "QF")
    private Integer QF;

    @Column(name = "QR")
    private Integer QR;

    @Column(name = "SQ")
    private Integer SQ;

    @Column(name = "VS")
    private Integer VS;

    @Column(name = "UA")
    private Integer UA;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Integer getAA() {
        return AA;
    }

    public void setAA(Integer AA) {
        this.AA = AA;
    }

    public Integer getAC() {
        return AC;
    }

    public void setAC(Integer AC) {
        this.AC = AC;
    }

    public Integer getAF() {
        return AF;
    }

    public void setAF(Integer AF) {
        this.AF = AF;
    }

    public Integer getAS() {
        return AS;
    }

    public void setAS(Integer AS) {
        this.AS = AS;
    }

    public Integer getBA() {
        return BA;
    }

    public void setBA(Integer BA) {
        this.BA = BA;
    }

    public Integer getCX() {
        return CX;
    }

    public void setCX(Integer CX) {
        this.CX = CX;
    }

    public Integer getDL() {
        return DL;
    }

    public void setDL(Integer DL) {
        this.DL = DL;
    }

    public Integer getEK() {
        return EK;
    }

    public void setEK(Integer EK) {
        this.EK = EK;
    }

    public Integer getEY() {
        return EY;
    }

    public void setEY(Integer EY) {
        this.EY = EY;
    }

    public Integer getJL() {
        return JL;
    }

    public void setJL(Integer JL) {
        this.JL = JL;
    }

    public Integer getLH() {
        return LH;
    }

    public void setLH(Integer LH) {
        this.LH = LH;
    }

    public Integer getNH() {
        return NH;
    }

    public void setNH(Integer NH) {
        this.NH = NH;
    }

    public Integer getQF() {
        return QF;
    }

    public void setQF(Integer QF) {
        this.QF = QF;
    }

    public Integer getQR() {
        return QR;
    }

    public void setQR(Integer QR) {
        this.QR = QR;
    }

    public Integer getSQ() {
        return SQ;
    }

    public void setSQ(Integer SQ) {
        this.SQ = SQ;
    }

    public Integer getVS() {
        return VS;
    }

    public void setVS(Integer VS) {
        this.VS = VS;
    }

    public Integer getUA() {
        return UA;
    }

    public void setUA(Integer UA) {
        this.UA = UA;
    }

    public ParserAnswer() {
    }

    public ParserAnswer(Long id,Request request, Integer AA, Integer AC, Integer AF, Integer AS, Integer BA, Integer CX, Integer DL, Integer EK, Integer EY, Integer JL, Integer LH, Integer NH, Integer QF, Integer QR, Integer SQ, Integer VS, Integer UA) {
        this.id = id;
        this.request = request;
        this.AA = AA;
        this.AC = AC;
        this.AF = AF;
        this.AS = AS;
        this.BA = BA;
        this.CX = CX;
        this.DL = DL;
        this.EK = EK;
        this.EY = EY;
        this.JL = JL;
        this.LH = LH;
        this.NH = NH;
        this.QF = QF;
        this.QR = QR;
        this.SQ = SQ;
        this.VS = VS;
        this.UA = UA;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParserAnswer that = (ParserAnswer) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (AA != null ? !AA.equals(that.AA) : that.AA != null) return false;
        if (AC != null ? !AC.equals(that.AC) : that.AC != null) return false;
        if (AF != null ? !AF.equals(that.AF) : that.AF != null) return false;
        if (AS != null ? !AS.equals(that.AS) : that.AS != null) return false;
        if (BA != null ? !BA.equals(that.BA) : that.BA != null) return false;
        if (CX != null ? !CX.equals(that.CX) : that.CX != null) return false;
        if (DL != null ? !DL.equals(that.DL) : that.DL != null) return false;
        if (EK != null ? !EK.equals(that.EK) : that.EK != null) return false;
        if (EY != null ? !EY.equals(that.EY) : that.EY != null) return false;
        if (JL != null ? !JL.equals(that.JL) : that.JL != null) return false;
        if (LH != null ? !LH.equals(that.LH) : that.LH != null) return false;
        if (NH != null ? !NH.equals(that.NH) : that.NH != null) return false;
        if (QF != null ? !QF.equals(that.QF) : that.QF != null) return false;
        if (QR != null ? !QR.equals(that.QR) : that.QR != null) return false;
        if (SQ != null ? !SQ.equals(that.SQ) : that.SQ != null) return false;
        if (VS != null ? !VS.equals(that.VS) : that.VS != null) return false;
        return !(UA != null ? !UA.equals(that.UA) : that.UA != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (AA != null ? AA.hashCode() : 0);
        result = 31 * result + (AC != null ? AC.hashCode() : 0);
        result = 31 * result + (AF != null ? AF.hashCode() : 0);
        result = 31 * result + (AS != null ? AS.hashCode() : 0);
        result = 31 * result + (BA != null ? BA.hashCode() : 0);
        result = 31 * result + (CX != null ? CX.hashCode() : 0);
        result = 31 * result + (DL != null ? DL.hashCode() : 0);
        result = 31 * result + (EK != null ? EK.hashCode() : 0);
        result = 31 * result + (EY != null ? EY.hashCode() : 0);
        result = 31 * result + (JL != null ? JL.hashCode() : 0);
        result = 31 * result + (LH != null ? LH.hashCode() : 0);
        result = 31 * result + (NH != null ? NH.hashCode() : 0);
        result = 31 * result + (QF != null ? QF.hashCode() : 0);
        result = 31 * result + (QR != null ? QR.hashCode() : 0);
        result = 31 * result + (SQ != null ? SQ.hashCode() : 0);
        result = 31 * result + (VS != null ? VS.hashCode() : 0);
        result = 31 * result + (UA != null ? UA.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ParserAnswer{" +
                "id=" + id +
                ", AA=" + AA +
                ", AC=" + AC +
                ", AF=" + AF +
                ", AS=" + AS +
                ", BA=" + BA +
                ", CX=" + CX +
                ", DL=" + DL +
                ", EK=" + EK +
                ", EY=" + EY +
                ", JL=" + JL +
                ", LH=" + LH +
                ", NH=" + NH +
                ", QF=" + QF +
                ", QR=" + QR +
                ", SQ=" + SQ +
                ", VS=" + VS +
                ", UA=" + UA +
                '}';
    }
}
