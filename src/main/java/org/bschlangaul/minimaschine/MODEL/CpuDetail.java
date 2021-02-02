package org.bschlangaul.minimaschine.MODEL;

class CpuDetail extends Cpu {
  private MikroSchritte mikroStatus = MikroSchritte.komplett;
  private int op1;
  private int op2;
  private int res;
  private int pcAlt = 0;

  CpuDetail(Speicher speicher) {
    super(speicher);
  }

  @Override
  public void ZurückSetzen() {
    this.mikroStatus = MikroSchritte.komplett;
    this.pcAlt = 0;
    super.ZurückSetzen();
  }

  @Override
  public void Schritt() {
    do {
      this.MikroSchritt();
    } while (this.mikroStatus != MikroSchritte.komplett);
  }

  @Override
  public void MikroSchritt() {
    switch (this.mikroStatus) {
      case komplett: {
        this.mikroStatus = MikroSchritte.fetch_opcode;
        this.Melden("", "", "", "", "", true, this.pc.WertGeben(), -1, this.sp.WertGeben(), "komplett");
        break;
      }
      case fetch_opcode: {
        this.pcAlt = this.pc.WertGeben();
        int n = this.speicher.WortOhneVorzeichenGeben(this.pcAlt);
        this.pc.Inkrementieren(1);
        this.adressmodus = n / 256;
        this.befehlscode = n % 256;
        this.mikroStatus = MikroSchritte.fetch_adressteil;
        this.Melden("" + n, "" + (this.pcAlt < 0 ? 65536 + this.pcAlt : this.pcAlt), "", "", "", false, this.pcAlt, -1,
            this.sp.WertGeben(), "fetch_op");
        break;
      }
      case fetch_adressteil: {
        this.pcAlt = this.pc.WertGeben();
        this.adresse = this.speicher.WortMitVorzeichenGeben(this.pcAlt);
        this.pc.Inkrementieren(1);
        if (this.adressmodus == 4 || this.adressmodus == 5) {
          String string = "" + this.adresse;
          this.adresse += this.sp.WertGeben();
          this.mikroStatus = this.adressmodus == 5 ? MikroSchritte.fetch_indirekt : MikroSchritte.decode;
          this.adressmodus = 1;
          this.Melden("" + this.adresse + "(SP)", "" + (this.pcAlt < 0 ? 65536 + this.pcAlt : this.pcAlt), string,
              "" + this.sp.WertGeben(), "" + this.adresse, false, -1, -1, -1, "fetch_adr");
          break;
        }
        if (this.adressmodus == 6) {
          String string = "" + this.adresse;
          this.adresse += this.sp.WertGeben();
          this.adressmodus = 2;
          this.mikroStatus = MikroSchritte.decode;
          this.Melden("$" + this.adresse + "(SP)", "" + (this.pcAlt < 0 ? 65536 + this.pcAlt : this.pcAlt), string,
              "" + this.sp.WertGeben(), "" + this.adresse, false, -1, -1, -1, "fetch_adr");
          break;
        }
        if (this.adressmodus == 3) {
          this.mikroStatus = MikroSchritte.fetch_indirekt;
          this.adressmodus = 1;
        } else {
          this.mikroStatus = MikroSchritte.decode;
        }
        this.Melden("" + this.adresse, "" + (this.pcAlt < 0 ? 65536 + this.pcAlt : this.pcAlt), "", "", "", false, -1,
            -1, -1, "fetch_adr");
        break;
      }
      case fetch_indirekt: {
        this.mikroStatus = MikroSchritte.decode;
        int n = this.adresse;
        this.adresse = this.speicher.WortMitVorzeichenGeben(this.adresse);
        this.Melden("" + this.adresse, "" + n, "", "", "", false, -1, -1, -1, "fetch_indir");
        break;
      }
      case decode: {
        this.OpcodeTesten();
        this.mikroStatus = MikroSchritte.execute_1;
        this.Melden("", "", "", "", "", true, -1, -1, -1, "dekode");
        break;
      }
      case execute_1: {
        this.Excecute1();
        break;
      }
      case execute_2: {
        this.Excecute2();
      }
    }
  }

  private void Excecute1() {
    block0: switch (this.befehlscode) {
      case 0: {
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "", "", "", true, -1, -1, -1, "exec_1");
        break;
      }
      case 99: {
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "", "", "", true, -1, -1, -1, "exec_1");
        break;
      }
      case 1: {
        this.ZurückSetzen();
        this.speicher.SpeicherLöschen();
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "", "", "", true, -1, -1, -1, "exec_1");
        break;
      }
      case 46: {
        this.op1 = this.a.WertGeben();
        this.mikroStatus = MikroSchritte.execute_2;
        this.Melden("", "", "" + this.op1, "", "", true, -1, -1, -1, "exec_1");
        break;
      }
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 40:
      case 41:
      case 42:
      case 43:
      case 44:
      case 45: {
        this.op1 = this.a.WertGeben();
        this.op2 = this.OperandenwertGeben(this.adresse, this.adressmodus);
        this.mikroStatus = MikroSchritte.execute_2;
        if (this.adressmodus == 1) {
          this.Melden("" + this.op2, "" + (this.adresse < 0 ? 65536 + this.adresse : this.adresse), "" + this.op1,
              "" + this.op2, "", true, -1, this.adresse, -1, "exec_1");
          break;
        }
        this.Melden("", "", "" + this.op1, "" + this.op2, "", true, -1, -1, -1, "exec_1");
        break;
      }
      case 20: {
        this.a.WertSetzen(this.OperandenwertGeben(this.adresse, this.adressmodus));
        this.ovflag = false;
        int n = this.a.WertGeben();
        this.ltflag = n < 0;
        this.eqflag = n == 0;
        this.mikroStatus = MikroSchritte.komplett;
        if (this.adressmodus == 1) {
          this.Melden("" + n, "" + (this.adresse < 0 ? 65536 + this.adresse : this.adresse), "", "", "", true, -1,
              this.adresse, -1, "exec_1");
          break;
        }
        this.Melden("", "", "", "", "", true, -1, -1, -1, "exec_1");
        break;
      }
      case 21: {
        this.speicher.WortSetzen(this.adresse, this.a.WertGeben());
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("" + this.a.WertGeben(), "" + (this.adresse < 0 ? 65536 + this.adresse : this.adresse), "", "", "",
            true, -1, this.adresse, -1, "exec_1");
        break;
      }
      case 30: {
        if (!this.ltflag && !this.eqflag) {
          this.pc.WertSetzen(this.adresse);
        }
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "", "", "", true, this.pc.WertGeben(), -1, -1, "exec_1");
        break;
      }
      case 31: {
        if (!this.ltflag) {
          this.pc.WertSetzen(this.adresse);
        }
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "", "", "", true, this.pc.WertGeben(), -1, -1, "exec_1");
        break;
      }
      case 32: {
        if (this.ltflag) {
          this.pc.WertSetzen(this.adresse);
        }
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "", "", "", true, this.pc.WertGeben(), -1, -1, "exec_1");
        break;
      }
      case 33: {
        if (this.ltflag || this.eqflag) {
          this.pc.WertSetzen(this.adresse);
        }
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "", "", "", true, this.pc.WertGeben(), -1, -1, "exec_1");
        break;
      }
      case 34: {
        if (this.eqflag) {
          this.pc.WertSetzen(this.adresse);
        }
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "", "", "", true, this.pc.WertGeben(), -1, -1, "exec_1");
        break;
      }
      case 35: {
        if (!this.eqflag) {
          this.pc.WertSetzen(this.adresse);
        }
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "", "", "", true, this.pc.WertGeben(), -1, -1, "exec_1");
        break;
      }
      case 37: {
        if (this.ovflag) {
          this.pc.WertSetzen(this.adresse);
        }
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "", "", "", true, this.pc.WertGeben(), -1, -1, "exec_1");
        break;
      }
      case 36: {
        this.pc.WertSetzen(this.adresse);
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "", "", "", true, this.pc.WertGeben(), -1, -1, "exec_1");
        break;
      }
      default: {
        if (this.erweitert) {
          switch (this.befehlscode) {
            case 25: {
              this.mikroStatus = MikroSchritte.execute_2;
              int n = this.sp.WertGeben() < 0 ? this.sp.WertGeben() + 65536 : this.sp.WertGeben();
              this.Melden("", "", "" + n, "-1", "" + (n - 1), true, -1, -1, -1, "exec_1");
              this.sp.Dekrementieren(1);
              break block0;
            }
            case 26: {
              this.mikroStatus = MikroSchritte.execute_2;
              this.a.WertSetzen(this.speicher.WortMitVorzeichenGeben(this.sp.WertGeben()));
              this.ovflag = false;
              int n = this.a.WertGeben();
              this.ltflag = n < 0;
              this.eqflag = n == 0;
              n = this.sp.WertGeben() < 0 ? this.sp.WertGeben() + 65536 : this.sp.WertGeben();
              this.Melden("" + this.a.WertGeben(), "" + n, "", "", "", true, -1, -1, this.sp.WertGeben(), "exec_1");
              break block0;
            }
            case 7: {
              this.mikroStatus = MikroSchritte.komplett;
              int n = this.sp.WertGeben() < 0 ? this.sp.WertGeben() + 65536 : this.sp.WertGeben();
              this.sp.Dekrementieren(this.adresse);
              int n2 = this.sp.WertGeben() < 0 ? this.sp.WertGeben() + 65536 : this.sp.WertGeben();
              this.Melden("", "", "" + n, "" + this.adresse, "" + n2, true, -1, -1, this.sp.WertGeben(), "exec_1");
              break block0;
            }
            case 8: {
              this.mikroStatus = MikroSchritte.komplett;
              int n = this.sp.WertGeben() < 0 ? this.sp.WertGeben() + 65536 : this.sp.WertGeben();
              this.sp.Inkrementieren(this.adresse);
              int n3 = this.sp.WertGeben() < 0 ? this.sp.WertGeben() + 65536 : this.sp.WertGeben();
              this.Melden("", "", "" + n, "" + this.adresse, "" + n3, true, -1, -1, this.sp.WertGeben(), "exec_1");
              break block0;
            }
            case 6: {
              this.mikroStatus = MikroSchritte.execute_2;
              int n = this.speicher.WortMitVorzeichenGeben(this.sp.WertGeben());
              this.pc.WertSetzen(n);
              this.Melden("" + (n < 0 ? n + 65536 : n),
                  "" + (this.sp.WertGeben() < 0 ? this.sp.WertGeben() + 65536 : this.sp.WertGeben()), "", "", "", true,
                  this.pc.WertGeben(), -1, -1, "exec_1");
              break block0;
            }
            case 5: {
              this.mikroStatus = MikroSchritte.execute_2;
              int n = this.sp.WertGeben() < 0 ? this.sp.WertGeben() + 65536 : this.sp.WertGeben();
              this.Melden("", "", "" + n, "-1", "" + (n - 1), true, -1, -1, -1, "exec_1");
              this.sp.Dekrementieren(1);
              break block0;
            }
          }
          this.mikroStatus = MikroSchritte.komplett;
          this.Melden("", "", "", "", "", false, -1, -1, -1, "exec_1");
          this.Fehlermeldung("Illegaler Befehlscode");
          this.befehlscode = -1;
          break;
        }
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "", "", "", false, -1, -1, -1, "exec_1");
        this.Fehlermeldung("Illegaler Befehlscode");
        this.befehlscode = -1;
      }
    }
  }

  private void Excecute2() {
    switch (this.befehlscode) {
      case 46: {
        this.res = ~this.op1;
        this.ovflag = false;
        this.a.WertSetzen(this.res);
        this.res = this.a.WertGeben();
        this.ltflag = this.res < 0;
        this.eqflag = this.res == 0;
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "" + this.op1, "", "" + this.res, true, -1, -1, -1, "exec_2");
        break;
      }
      case 10: {
        this.res = this.op1 + this.op2;
        this.ovflag = this.res > 32767 || this.res < -32768;
        this.a.WertSetzen(this.res);
        this.res = this.a.WertGeben();
        this.ltflag = this.res < 0;
        this.eqflag = this.res == 0;
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "" + this.op1, "" + this.op2, "" + this.res, true, -1, -1, -1, "exec_2");
        break;
      }
      case 11: {
        this.res = this.op1 - this.op2;
        this.ovflag = this.res > 32767 || this.res < -32768;
        this.a.WertSetzen(this.res);
        this.res = this.a.WertGeben();
        this.ltflag = this.res < 0;
        this.eqflag = this.res == 0;
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "" + this.op1, "" + this.op2, "" + this.res, true, -1, -1, -1, "exec_2");
        break;
      }
      case 12: {
        this.res = this.op1 * this.op2;
        this.ovflag = this.res > 32767 || this.res < -32768;
        this.a.WertSetzen(this.res);
        this.res = this.a.WertGeben();
        this.ltflag = this.res < 0;
        this.eqflag = this.res == 0;
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "" + this.op1, "" + this.op2, "" + this.res, true, -1, -1, -1, "exec_2");
        break;
      }
      case 13: {
        if (this.op2 == 0) {
          this.Fehlermeldung("Division durch 0");
          this.res = this.op1;
          this.ovflag = true;
        } else {
          this.res = this.op1 / this.op2;
          this.ovflag = this.res > 32767 || this.res < -32768;
        }
        this.a.WertSetzen(this.res);
        this.res = this.a.WertGeben();
        this.ltflag = this.res < 0;
        this.eqflag = this.res == 0;
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "" + this.op1, "" + this.op2, "" + this.res, true, -1, -1, -1, "exec_2");
        break;
      }
      case 14: {
        if (this.op2 == 0) {
          this.Fehlermeldung("Division durch 0");
          this.res = this.op1;
          this.ovflag = true;
        } else {
          this.res = this.op1 % this.op2;
          this.ovflag = this.res > 32767 || this.res < -32768;
        }
        this.a.WertSetzen(this.res);
        this.res = this.a.WertGeben();
        this.ltflag = this.res < 0;
        this.eqflag = this.res == 0;
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "" + this.op1, "" + this.op2, "" + this.res, true, -1, -1, -1, "exec_2");
        break;
      }
      case 15: {
        this.ovflag = false;
        this.ltflag = this.op1 < this.op2;
        this.eqflag = this.op1 == this.op2;
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "" + this.op1, "" + this.op2, "", true, -1, -1, -1, "exec_2");
        break;
      }
      case 40: {
        this.res = this.op1 & this.op2;
        this.ovflag = false;
        this.a.WertSetzen(this.res);
        this.res = this.a.WertGeben();
        this.ltflag = this.res < 0;
        this.eqflag = this.res == 0;
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "" + this.op1, "" + this.op2, "" + this.res, true, -1, -1, -1, "exec_2");
        break;
      }
      case 41: {
        this.res = this.op1 | this.op2;
        this.ovflag = false;
        this.a.WertSetzen(this.res);
        this.res = this.a.WertGeben();
        this.ltflag = this.res < 0;
        this.eqflag = this.res == 0;
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "" + this.op1, "" + this.op2, "" + this.res, true, -1, -1, -1, "exec_2");
        break;
      }
      case 42: {
        this.res = this.op1 ^ this.op2;
        this.ovflag = false;
        this.a.WertSetzen(this.res);
        this.res = this.a.WertGeben();
        this.ltflag = this.res < 0;
        this.eqflag = this.res == 0;
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "" + this.op1, "" + this.op2, "" + this.res, true, -1, -1, -1, "exec_2");
        break;
      }
      case 43: {
        this.res = this.op1 << this.op2;
        this.ovflag = false;
        this.a.WertSetzen(this.res);
        this.res = this.a.WertGeben();
        this.ltflag = this.res < 0;
        this.eqflag = this.res == 0;
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "" + this.op1, "" + this.op2, "" + this.res, true, -1, -1, -1, "exec_2");
        break;
      }
      case 44: {
        this.res = (this.op1 & 0xFFFF) >> this.op2;
        this.ovflag = false;
        this.a.WertSetzen(this.res);
        this.res = this.a.WertGeben();
        this.ltflag = this.res < 0;
        this.eqflag = this.res == 0;
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "" + this.op1, "" + this.op2, "" + this.res, true, -1, -1, -1, "exec_2");
        break;
      }
      case 45: {
        this.res = this.op1 >> this.op2;
        this.ovflag = false;
        this.a.WertSetzen(this.res);
        this.res = this.a.WertGeben();
        this.ltflag = this.res < 0;
        this.eqflag = this.res == 0;
        this.mikroStatus = MikroSchritte.komplett;
        this.Melden("", "", "" + this.op1, "" + this.op2, "" + this.res, true, -1, -1, -1, "exec_2");
        break;
      }
      case 25: {
        this.mikroStatus = MikroSchritte.komplett;
        this.res = this.sp.WertGeben();
        this.Melden("" + this.a.WertGeben(), "" + (this.res < 0 ? 65536 + this.res : this.res), "", "", "", true, -1,
            -1, this.res, "exec_2");
        this.speicher.WortSetzen(this.res, this.a.WertGeben());
        break;
      }
      case 6:
      case 26: {
        this.mikroStatus = MikroSchritte.komplett;
        this.res = this.sp.WertGeben() < 0 ? this.sp.WertGeben() + 65536 : this.sp.WertGeben();
        this.Melden("", "", "" + this.res, "1", "" + (this.res + 1), true, -1, -1, -1, "exec_2");
        this.sp.Inkrementieren(1);
        break;
      }
      case 5: {
        this.mikroStatus = MikroSchritte.komplett;
        this.speicher.WortSetzen(this.sp.WertGeben(), this.pc.WertGeben());
        this.res = this.pc.WertGeben() < 0 ? this.pc.WertGeben() + 65536 : this.pc.WertGeben();
        this.pc.WertSetzen(this.adresse);
        this.Melden("" + this.res, "" + (this.sp.WertGeben() < 0 ? this.sp.WertGeben() + 65536 : this.sp.WertGeben()),
            "", "", "", true, this.pc.WertGeben(), -1, -1, "exec_2");
      }
    }
  }

  @Override
  public void Übertragen(Cpu cpu) {
    if (this != cpu) {
      while (this.mikroStatus != MikroSchritte.komplett) {
        this.MikroSchritt();
      }
    }
    super.Übertragen(cpu);
  }
}
