package org.bschlangaul.minimaschine.MODEL;

class CpuEinfach extends Cpu {
  CpuEinfach(Speicher speicher) {
    super(speicher);
  }

  @Override
  public void Schritt() {
    this.befehlscode = this.speicher.WortOhneVorzeichenGeben(this.pc.WertGeben());
    this.pc.Inkrementieren(1);
    this.adressmodus = this.befehlscode / 256;
    this.befehlscode %= 256;
    this.adresse = this.speicher.WortMitVorzeichenGeben(this.pc.WertGeben());
    this.OpcodeTesten();
    this.pc.Inkrementieren(1);
    block0: switch (this.befehlscode) {
      case 0: {
        break;
      }
      case 99: {
        break;
      }
      case 1: {
        this.ZurückSetzen();
        this.speicher.SpeicherLöschen();
        break;
      }
      case 46: {
        int n = this.a.WertGeben();
        int n2 = ~n;
        this.ovflag = false;
        this.a.WertSetzen(n2);
        n2 = this.a.WertGeben();
        this.ltflag = n2 < 0;
        this.eqflag = n2 == 0;
      }
      case 10: {
        int n = this.a.WertGeben();
        int n3 = this.OperandenwertGeben(this.adresse, this.adressmodus);
        int n2 = n + n3;
        this.ovflag = n2 > 32767 || n2 < -32768;
        this.a.WertSetzen(n2);
        n2 = this.a.WertGeben();
        this.ltflag = n2 < 0;
        this.eqflag = n2 == 0;
        break;
      }
      case 11: {
        int n = this.a.WertGeben();
        int n4 = this.OperandenwertGeben(this.adresse, this.adressmodus);
        int n5 = n - n4;
        this.ovflag = n5 > 32767 || n5 < -32768;
        this.a.WertSetzen(n5);
        n5 = this.a.WertGeben();
        this.ltflag = n5 < 0;
        this.eqflag = n5 == 0;
        break;
      }
      case 12: {
        int n = this.a.WertGeben();
        int n6 = this.OperandenwertGeben(this.adresse, this.adressmodus);
        int n7 = n * n6;
        this.ovflag = n7 > 32767 || n7 < -32768;
        this.a.WertSetzen(n7);
        n7 = this.a.WertGeben();
        this.ltflag = n7 < 0;
        this.eqflag = n7 == 0;
        break;
      }
      case 13: {
        int n;
        int n8 = this.a.WertGeben();
        int n9 = this.OperandenwertGeben(this.adresse, this.adressmodus);
        if (n9 == 0) {
          this.Fehlermeldung("Division durch 0");
          n = n8;
          this.ovflag = true;
        } else {
          n = n8 / n9;
          this.ovflag = n > 32767 || n < -32768;
        }
        this.a.WertSetzen(n);
        n = this.a.WertGeben();
        this.ltflag = n < 0;
        this.eqflag = n == 0;
        break;
      }
      case 14: {
        int n;
        int n10 = this.a.WertGeben();
        int n11 = this.OperandenwertGeben(this.adresse, this.adressmodus);
        if (n11 == 0) {
          this.Fehlermeldung("Division durch 0");
          n = 0;
          this.ovflag = true;
        } else {
          n = n10 % n11;
          this.ovflag = n > 32767 || n < -32768;
        }
        this.a.WertSetzen(n);
        n = this.a.WertGeben();
        this.ltflag = n < 0;
        this.eqflag = n == 0;
        break;
      }
      case 15: {
        int n = this.a.WertGeben();
        int n12 = this.OperandenwertGeben(this.adresse, this.adressmodus);
        this.ovflag = false;
        this.ltflag = n < n12;
        this.eqflag = n == n12;
        break;
      }
      case 40: {
        int n = this.a.WertGeben();
        int n13 = this.OperandenwertGeben(this.adresse, this.adressmodus);
        int n14 = n & n13;
        this.ovflag = false;
        this.a.WertSetzen(n14);
        n14 = this.a.WertGeben();
        this.ltflag = n14 < 0;
        this.eqflag = n14 == 0;
        break;
      }
      case 41: {
        int n = this.a.WertGeben();
        int n15 = this.OperandenwertGeben(this.adresse, this.adressmodus);
        int n16 = n | n15;
        this.ovflag = false;
        this.a.WertSetzen(n16);
        n16 = this.a.WertGeben();
        this.ltflag = n16 < 0;
        this.eqflag = n16 == 0;
        break;
      }
      case 42: {
        int n = this.a.WertGeben();
        int n17 = this.OperandenwertGeben(this.adresse, this.adressmodus);
        int n18 = n ^ n17;
        this.ovflag = false;
        this.a.WertSetzen(n18);
        n18 = this.a.WertGeben();
        this.ltflag = n18 < 0;
        this.eqflag = n18 == 0;
        break;
      }
      case 43: {
        int n = this.a.WertGeben();
        int n19 = this.OperandenwertGeben(this.adresse, this.adressmodus);
        int n20 = n << n19;
        this.ovflag = false;
        this.a.WertSetzen(n20);
        n20 = this.a.WertGeben();
        this.ltflag = n20 < 0;
        this.eqflag = n20 == 0;
        break;
      }
      case 44: {
        int n = this.a.WertGeben();
        int n21 = this.OperandenwertGeben(this.adresse, this.adressmodus);
        int n22 = (n & 0xFFFF) >> n21;
        this.ovflag = false;
        this.a.WertSetzen(n22);
        n22 = this.a.WertGeben();
        this.ltflag = n22 < 0;
        this.eqflag = n22 == 0;
        break;
      }
      case 45: {
        int n = this.a.WertGeben();
        int n23 = this.OperandenwertGeben(this.adresse, this.adressmodus);
        int n24 = n >> n23;
        this.ovflag = false;
        this.a.WertSetzen(n24);
        n24 = this.a.WertGeben();
        this.ltflag = n24 < 0;
        this.eqflag = n24 == 0;
        break;
      }
      case 20: {
        this.a.WertSetzen(this.OperandenwertGeben(this.adresse, this.adressmodus));
        this.ovflag = false;
        int n = this.a.WertGeben();
        this.ltflag = n < 0;
        this.eqflag = n == 0;
        break;
      }
      case 21: {
        if (this.adressmodus == 3) {
          this.adresse = this.speicher.WortOhneVorzeichenGeben(this.adresse);
        } else if (this.adressmodus == 4) {
          this.adresse += this.sp.WertGeben();
        } else if (this.adressmodus == 5) {
          this.adresse = this.speicher.WortOhneVorzeichenGeben(this.adresse + this.sp.WertGeben());
        }
        this.speicher.WortSetzen(this.adresse, this.a.WertGeben());
        break;
      }
      case 30: {
        if (this.ltflag || this.eqflag)
          break;
        this.pc.WertSetzen(this.adresse);
        break;
      }
      case 31: {
        if (this.ltflag)
          break;
        this.pc.WertSetzen(this.adresse);
        break;
      }
      case 32: {
        if (!this.ltflag)
          break;
        this.pc.WertSetzen(this.adresse);
        break;
      }
      case 33: {
        if (!this.ltflag && !this.eqflag)
          break;
        this.pc.WertSetzen(this.adresse);
        break;
      }
      case 34: {
        if (!this.eqflag)
          break;
        this.pc.WertSetzen(this.adresse);
        break;
      }
      case 35: {
        if (this.eqflag)
          break;
        this.pc.WertSetzen(this.adresse);
        break;
      }
      case 37: {
        if (!this.ovflag)
          break;
        this.pc.WertSetzen(this.adresse);
        break;
      }
      case 36: {
        this.pc.WertSetzen(this.adresse);
        break;
      }
      default: {
        if (this.erweitert) {
          switch (this.befehlscode) {
            case 25: {
              this.sp.Dekrementieren(1);
              this.speicher.WortSetzen(this.sp.WertGeben(), this.a.WertGeben());
              break block0;
            }
            case 26: {
              this.a.WertSetzen(this.speicher.WortMitVorzeichenGeben(this.sp.WertGeben()));
              this.sp.Inkrementieren(1);
              this.ovflag = false;
              int n = this.a.WertGeben();
              this.ltflag = n < 0;
              this.eqflag = n == 0;
              break block0;
            }
            case 7: {
              this.sp.Dekrementieren(this.adresse);
              break block0;
            }
            case 8: {
              this.sp.Inkrementieren(this.adresse);
              break block0;
            }
            case 6: {
              this.pc.WertSetzen(this.speicher.WortMitVorzeichenGeben(this.sp.WertGeben()));
              this.sp.Inkrementieren(1);
              break block0;
            }
            case 5: {
              this.sp.Dekrementieren(1);
              this.speicher.WortSetzen(this.sp.WertGeben(), this.pc.WertGeben());
              this.pc.WertSetzen(this.adresse);
              break block0;
            }
          }
          this.Fehlermeldung("Illegaler Befehlscode");
          this.befehlscode = -1;
          break;
        }
        this.Fehlermeldung("Illegaler Befehlscode");
        this.befehlscode = -1;
      }
    }
    this.Melden("", "", "", "", "", true, this.pc.WertGeben() - 2, this.adressmodus == 1 ? this.adresse : -1,
        this.sp.WertGeben(), "");
  }

  @Override
  public void MikroSchritt() {
  }
}
