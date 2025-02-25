import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Abstract class (Soyut sınıf)
abstract class Kisi {
    protected String ad;
    protected String soyad;

    // Yapılandırıcı (Constructor)
    public Kisi(String ad, String soyad) {
        this.ad = ad;
        this.soyad = soyad;
    }

    // Metot (Örnek bir metot)
    public void bilgileriGoster() {
        System.out.println("Ad: " + ad + ", Soyad: " + soyad);
    }
}

// Kalıtım (Hasta ve Doktor sınıfları Kisi sınıfından türetildi)
class Hasta extends Kisi {
    private String hastalik;

    public Hasta(String ad, String soyad, String hastalik) {
        super(ad, soyad);
        this.hastalik = hastalik;
    }

    public void randevuAl(String tarih) {
        System.out.println(ad + " " + soyad + " için randevu alındı: " + tarih);
    }

    public void randevuAl(String tarih, String saat) {  // Overloading 1
        System.out.println(ad + " " + soyad + " için randevu alındı: " + tarih + " " + saat);
    }

    public String getHastalik() {
        return hastalik;
    }
}

class Doktor extends Kisi {
    private String uzmanlik;

    public Doktor(String ad, String soyad, String uzmanlik) {
        super(ad, soyad);
        this.uzmanlik = uzmanlik;
    }

    public void hastaGoruntule(Hasta hasta) {
        System.out.println("Doktor " + ad + " " + soyad + ", Hasta: " + hasta.ad + " " + hasta.soyad);
    }
}

// Arayüz (Interface)
interface RandevuYonetimi {
    void randevuAl(Hasta hasta, Doktor doktor, String tarih);
    void randevuIptalEt(int randevuID);
}

// İç içe class (Nested Class)
class RandevuSistemi {
    private static int sayac = 0;
    private List<String> randevuListesi = new ArrayList<>();

    // İç içe sınıf (Static inner class)
    static class Randevu {
        int id;
        String hasta;
        String doktor;
        String tarih;

        Randevu(int id, String hasta, String doktor, String tarih) {
            this.id = id;
            this.hasta = hasta;
            this.doktor = doktor;
            this.tarih = tarih;
        }

        void bilgileriYazdir() {
            System.out.println("Randevu ID: " + id + " - Hasta: " + hasta + " - Doktor: " + doktor + " - Tarih: " + tarih);
        }
    }

    public void randevuEkle(Hasta hasta, Doktor doktor, String tarih) {
        sayac++;
        Randevu yeniRandevu = new Randevu(sayac, hasta.ad, doktor.ad, tarih);
        yeniRandevu.bilgileriYazdir();
    }
}

// Exception handling (Özel hata yönetimi)
class RandevuException extends Exception {
    public RandevuException(String mesaj) {
        super(mesaj);
    }
}

public class HastaneRandevuSistemi {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Kullanıcıdan hasta bilgilerini alma
        System.out.print("Hasta Adı: ");
        String hastaAdi = scanner.nextLine();

        System.out.print("Hasta Soyadı: ");
        String hastaSoyadi = scanner.nextLine();

        System.out.print("Hastalık: ");
        String hastalik = scanner.nextLine();

        Hasta hasta = new Hasta(hastaAdi, hastaSoyadi, hastalik);

        // Kullanıcıdan doktor bilgilerini alma
        System.out.print("Doktor Adı: ");
        String doktorAdi = scanner.nextLine();

        System.out.print("Doktor Soyadı: ");
        String doktorSoyadi = scanner.nextLine();

        System.out.print("Uzmanlık: ");
        String uzmanlik = scanner.nextLine();

        Doktor doktor = new Doktor(doktorAdi, doktorSoyadi, uzmanlik);

        // Kullanıcıdan randevu tarihi alma
        System.out.print("Randevu Tarihi (YYYY-MM-DD): ");
        String randevuTarihi = scanner.nextLine();

        // Randevu alma işlemi
        RandevuSistemi randevuSistemi = new RandevuSistemi();
        randevuSistemi.randevuEkle(hasta, doktor, randevuTarihi);

        // Hata yönetimi ile randevu iptali
        try {
            System.out.print("İptal etmek istediğiniz randevu ID'sini girin: ");
            int randevuID = scanner.nextInt();
            if (randevuID <= 0) {
                throw new RandevuException("Geçersiz randevu ID!");
            }
            System.out.println("Randevu iptal edildi. ID: " + randevuID);
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }

        scanner.close();
    }
}
