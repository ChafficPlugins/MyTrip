package de.chaffic.MyTrip.API;

import de.chaffic.MyTrip.Main;
import org.bukkit.ChatColor;

import java.util.logging.Logger;

public class LanguageAPI {
    //TODO replace with xml language file
    private static final Main plugin = Main.getPlugin(Main.class);

    String l1 = "quality";
    String l2 = "consume message";
    String l3 = "no permissions";
    String l4 = "list commands";
    String l5 = "sobered up";
    String l6 = "not high";
    String l7 = "only players";
    String l8 = "drug set";
    String l9 = "anti toxin";
    String l10 = "Right click";
    String l11 = "You received";
    String l12 = "not exist";
    String l13 = "State";
    String l15 = "config reload failed";
    String l16 = "incorrect code";
    String l17 = "drugs";
    String l18 = "InvCreation 1";
    String l19 = "InvCreation 2";
    String l20 = "InvCreation 3";
    String l21 = "drug test";
    String l22 = "is high";
    String l23 = "not high";
    String l24 = "addiction";
    String l25 = "consume";
    String l26 = "correct code";
    String l27 = "not on server";
    String l28 = "no longer addicted";
    String l29 = "Edit";
    String l30 = "Shift crafting";

    public void setLang(String lang, Logger logger){
        logger.info("Selected language: " + lang);
        plugin.fc.getLanguage().addDefault(l1, "Quality");
        plugin.fc.getLanguage().addDefault(l2, "You consumed");
        plugin.fc.getLanguage().addDefault(l3, "You do not have the permissions to do this!");
        plugin.fc.getLanguage().addDefault(l4, "MyTrip Commands");
        plugin.fc.getLanguage().addDefault(l5, "Sobered up!");
        plugin.fc.getLanguage().addDefault(l6, "You're not high!");
        plugin.fc.getLanguage().addDefault(l7, "This is an only players command.");
        plugin.fc.getLanguage().addDefault(l8, "drug set");
        plugin.fc.getLanguage().addDefault(l9, "anti toxin");
        plugin.fc.getLanguage().addDefault(l10, "Right click");
        plugin.fc.getLanguage().addDefault(l11, "You received");
        plugin.fc.getLanguage().addDefault(l12, "does not exist!");
        plugin.fc.getLanguage().addDefault(l13, "State");
        plugin.fc.getLanguage().addDefault(l15, "Could not reload config files");
        plugin.fc.getLanguage().addDefault(l16, "is not correct");
        plugin.fc.getLanguage().addDefault(l17, "drugs");
        plugin.fc.getLanguage().addDefault(l18, "How to craft your drug");
        plugin.fc.getLanguage().addDefault(l19, "Click at effects");
        plugin.fc.getLanguage().addDefault(l20, "Activate fx-effects");
        plugin.fc.getLanguage().addDefault(l21, "drug test");
        plugin.fc.getLanguage().addDefault(l22, "is high.");
        plugin.fc.getLanguage().addDefault(l23, "is not high.");
        plugin.fc.getLanguage().addDefault(l24, "Drug Addiction");
        plugin.fc.getLanguage().addDefault(l25, "To feel better consume ");
        plugin.fc.getLanguage().addDefault(l26, "You redeemed a correct code.");
        plugin.fc.getLanguage().addDefault(l27, "is not on the server.");
        plugin.fc.getLanguage().addDefault(l28, "is no longer addicted.");
        plugin.fc.getLanguage().addDefault(l29, "Edit");
        plugin.fc.getLanguage().addDefault(l30, "Shift crafting drugs is not possible!");
        switch(lang.toUpperCase()){
            case "ENGLISH":
                plugin.setWord(l1, "Quality");
                plugin.setWord(l2, "You consumed");
                plugin.setWord(l3, "You do not have the permissions to do this!");
                plugin.setWord(l4, "MyTrip Commands");
                plugin.setWord(l5, "Sobered up!");
                plugin.setWord(l6, "You're not high!");
                plugin.setWord(l7, "This is an only players command.");
                plugin.setWord(l8, "drug set");
                plugin.setWord(l9, "anti toxin");
                plugin.setWord(l10, "Right click");
                plugin.setWord(l11, "You received");
                plugin.setWord(l12, "does not exist!");
                plugin.setWord(l13, "State");
                plugin.setWord(l15, "Could not reload config files");
                plugin.setWord(l16, "is not correct");
                plugin.setWord(l17, "drugs");
                plugin.setWord(l18, "How to craft your drug");
                plugin.setWord(l19, "Click at effects");
                plugin.setWord(l20, "Activate fx-effects");
                plugin.setWord(l21, "drug test");
                plugin.setWord(l22, "is high.");
                plugin.setWord(l23, "is not high.");
                plugin.setWord(l24, "Drug Addiction");
                plugin.setWord(l25, "To feel better consume ");
                plugin.setWord(l26, "You redeemed a correct code.");
                plugin.setWord(l27, "is not on the server.");
                plugin.setWord(l28, "is no longer addicted.");
                plugin.setWord(l29, "Edit");
                plugin.setWord(l30, "Shift crafting drugs is not possible!");
                logger.info("Using english language config.");
                break;
            case "GERMAN":
                plugin.setWord(l1, "Qualität");
                plugin.setWord(l2, "Du konsumierst");
                plugin.setWord(l3, "Du hast nicht die Berechtigung, dies zu tun!");
                plugin.setWord(l4, "MyTrip Befehle");
                plugin.setWord(l5, "Du bist ausgenüchtert!");
                plugin.setWord(l6, "Du bist nicht high!");
                plugin.setWord(l7, "Dieser Command kann nur von Spielern ausgeführt werden.");
                plugin.setWord(l8, "Drogenset");
                plugin.setWord(l9, "Antitoxin");
                plugin.setWord(l10, "Rechtsklick");
                plugin.setWord(l11, "Du erhältst");
                plugin.setWord(l12, "existiert nicht!");
                plugin.setWord(l13, "Status");
                plugin.setWord(l15, "Konfigurationsdateien konnten nicht neu geladen werden");
                plugin.setWord(l16, "ist nicht korrekt.");
                plugin.setWord(l17, "Drogen");
                plugin.setWord(l18, "Drogenbestandteil hier einfügen.");
                plugin.setWord(l19, "Klicke die Effekte an.");
                plugin.setWord(l20, "Fx-Effekte aktivieren");
                plugin.setWord(l21, "Drogentest");
                plugin.setWord(l22, "ist high.");
                plugin.setWord(l23, "ist nicht high.");
                plugin.setWord(l24, "Drogensucht");
                plugin.setWord(l25, "Um dich besser zu fühlen ");
                plugin.setWord(l26, "Du hast einen korrekten Code eingelöst.");
                plugin.setWord(l27, "ist nicht auf dem Server.");
                plugin.setWord(l28, "ist nicht mehr süchtig.");
                plugin.setWord(l29, "Bearbeiten");
                plugin.setWord(l30, "Shift crafting drugs is not possible!");
                logger.info("Using german language config.");
                break;
            case "LITHUANIAN":
                plugin.setWord(l1, "Kokybė");
                plugin.setWord(l2, "Tu suvartojai");
                plugin.setWord(l3, "Tu neturi leidimo tai daryti!");
                plugin.setWord(l4, "MyTrip komandos");
                plugin.setWord(l5, "Išsiblaivei!");
                plugin.setWord(l6, "Tu neapsirūkęs!");
                plugin.setWord(l7, "Tai yra tik žaidėjų komanda.");
                plugin.setWord(l8, "narkotikų rinkinys");
                plugin.setWord(l9, "anti toksinas");
                plugin.setWord(l10, "Spausk dešinįjį pelės mygtuką");
                plugin.setWord(l11, "Tu gavai!");
                plugin.setWord(l12, "neegzistuoja!");
                plugin.setWord(l13, "Buklė");
                plugin.setWord(l15, "Nepavyko įkelti konfigūracijos failų");
                plugin.setWord(l16, "Tai nėra teisinga");
                plugin.setWord(l17, "narkotikai");
                plugin.setWord(l18, "Įdėti narkotikų ingredientą");
                plugin.setWord(l19, "Paspausk ant efektų");
                plugin.setWord(l20, "Aktyvuoti fx efektus");
                plugin.setWord(l21, "apsirūkimo testas");
                plugin.setWord(l22, "yra apsirūkęs.");
                plugin.setWord(l23, "nėra apsirūkęs.");
                plugin.setWord(l24, "Narkotikų priklausomybė");
                plugin.setWord(l25, "Norėdami geriau jaustis vartokite ");
                plugin.setWord(l26, "Tu išpirkai teisingą kodą.");
                plugin.setWord(l27, "nėra prisijungęs serveryje.");
                plugin.setWord(l28, "nebeturi priklausomybės.");
                plugin.setWord(l29, "Redaguoti");
                plugin.setWord(l30, "Shift crafting drugs is not possible!");
                logger.info("Using lithuanian language config.");
                break;
            case "ARABIC":
                plugin.setWord(l1, "جودة");
                plugin.setWord(l2, "انت استهلكت");
                plugin.setWord(l3, "ليس لديك الصلاحيات للقيام بذلك!");
                plugin.setWord(l4, "أوامر MyTrip");
                plugin.setWord(l5, "تم إيقاظك!!");
                plugin.setWord(l6, "أنت لست سكراناً!");
                plugin.setWord(l7, "هذا الأمر للاعبين فقط.");
                plugin.setWord(l8, "عدة المخدرات");
                plugin.setWord(l9, "مكافح السموم");
                plugin.setWord(l10, "الزر الأيمن");
                plugin.setWord(l11, "انت استلمت");
                plugin.setWord(l12, "لا يوجد!");
                plugin.setWord(l13, "حالة");
                plugin.setWord(l15, "تعذر إعادة تحميل ملف الاعدادات");
                plugin.setWord(l16, "غير صحيح");
                plugin.setWord(l17, "مخدرات");
                plugin.setWord(l18, "أضف مكون المخدرات");
                plugin.setWord(l19, "اضغط علي التأثيرات");
                plugin.setWord(l20, "تنشيط التأثيرات المميزة");
                plugin.setWord(l21, "اختبار المخدرات");
                plugin.setWord(l22, "سكران.");
                plugin.setWord(l23, "ليس سكران.");
                plugin.setWord(l24, "ادمان المخدرات");
                plugin.setWord(l25, "لتشعر بحال افضل استهلك");
                plugin.setWord(l26, "لقد استخدمت رمز صحيح.");
                plugin.setWord(l27, "ليس في السيرفر.");
                plugin.setWord(l28, "لم يعد مدمناً.");
                plugin.setWord(l29, "تعديل");
                plugin.setWord(l30, "Shift crafting drugs is not possible!");
                logger.info("Using arabic language config.");
                break;
            case "FRENCH":
                plugin.setWord(l1, "Qualité");
                plugin.setWord(l2, "Vous avez consommé");
                plugin.setWord(l3, "Vous n'avez pas la permission de faire cela !");
                plugin.setWord(l4, "Commandes de \"MyTrip\"");
                plugin.setWord(l5, "Vous êtes sobre.");
                plugin.setWord(l6, "Vous ne planez pas.");
                plugin.setWord(l7, "Ceci est UNIQUEMENT une commande pour les joueurs.");
                plugin.setWord(l8, "drug set");
                plugin.setWord(l9, "Médi-cure");
                plugin.setWord(l10, "Clique droit");
                plugin.setWord(l11, "Vous avez reçu");
                plugin.setWord(l12, "n'existe pas !");
                plugin.setWord(l13, "État");
                plugin.setWord(l15, "Impossible de recharger les fichiers de configuration");
                plugin.setWord(l16, "n'est pas correct.");
                plugin.setWord(l17, "Drogues");
                plugin.setWord(l18, "Mettre dans un ingrédient");
                plugin.setWord(l19, "Clique sur les effets");
                plugin.setWord(l20, "Activer les effets spéciaux");
                plugin.setWord(l21, "Test de dépistage");
                plugin.setWord(l22, "plane.");
                plugin.setWord(l23, "est sobre.");
                plugin.setWord(l24, "Addiction à la drogue");
                plugin.setWord(l25, "Pour vous sentir mieux, consommez ");
                plugin.setWord(l26, "Vous avez utilisé un code correct.");
                plugin.setWord(l27, "n'est pas sur le serveur.");
                plugin.setWord(l28, "n'est plus dépendant.");
                plugin.setWord(l29, "Éditer");
                plugin.setWord(l30, "Shift crafting drugs is not possible!");
                logger.info("Using french language config.");
                break;
            case "SPANISH":
                plugin.setWord(l1, "Calidad");
                plugin.setWord(l2, "Has consumido");
                plugin.setWord(l3, "¡No tienes permisos para hacer esto!");
                plugin.setWord(l4, "Comandos de MyTrip");
                plugin.setWord(l5, "¡Estás chill!");
                plugin.setWord(l6, "No va drogao.");
                plugin.setWord(l7, "Este es un comando para jugadores.");
                plugin.setWord(l8, "Mesa de drogas");
                plugin.setWord(l9, "Antitoxina");
                plugin.setWord(l10, "Click derecho");
                plugin.setWord(l11, "Recibiste");
                plugin.setWord(l12, "No existe!");
                plugin.setWord(l13, "Estado");
                plugin.setWord(l15, "No se pudieron recargar los archivos de configuración");
                plugin.setWord(l16, "No és correcto.");
                plugin.setWord(l17, "Drogas");
                plugin.setWord(l18, "Poner en un ingrediente");
                plugin.setWord(l19, "Haga clic en los efectos");
                plugin.setWord(l20, "Permitir efectos especiales");
                plugin.setWord(l21, "Drogotest");
                plugin.setWord(l22, "va drogao.");
                plugin.setWord(l23, "no está drogado.");
                plugin.setWord(l24, "Adicción de drogas");
                plugin.setWord(l25, "Para sentir mejor consume ");
                plugin.setWord(l26, "Has canjeado un código correcto.");
                plugin.setWord(l27, "no está en el servidor.");
                plugin.setWord(l28, "ya no és un drogadicto.");
                plugin.setWord(l29, "Editar");
                plugin.setWord(l30, "Pulsar shift para craftear no és posible!");
                logger.info("Using spanish language config.");
                break;
            case "RUSSIAN":
                plugin.setWord(l1, "Качество");
                plugin.setWord(l2, "Вы употребили");
                plugin.setWord(l3, "У Вас недостаточно прав для этого!");
                plugin.setWord(l4, "Команды MyTrip");
                plugin.setWord(l5, "Протрезвел!");
                plugin.setWord(l6, "Вы не под кайфом!");
                plugin.setWord(l7, "Эта команда только для игроков.");
                plugin.setWord(l8, "Нарколаборатория");
                plugin.setWord(l9, "Анти-токсин");
                plugin.setWord(l10, "Правый клик");
                plugin.setWord(l11, "Вы получили");
                plugin.setWord(l12, "не существует!");
                plugin.setWord(l13, "Состояние");
                plugin.setWord(l15, "Не удалось перезагрузить файл конфигурации");
                plugin.setWord(l16, "не верно");
                plugin.setWord(l17, "наркотики");
                plugin.setWord(l18, "Как создать Ваш наркотик");
                plugin.setWord(l19, "Эффект при клике");
                plugin.setWord(l20, "Активировать FX-эффекты");
                plugin.setWord(l21, "Тест на наркотики");
                plugin.setWord(l22, "находится под действием наркотиков.");
                plugin.setWord(l23, "не находится под действием наркотиков.");
                plugin.setWord(l24, "Наркотическая зависимость");
                plugin.setWord(l25, "Чтобы чувствовать себя лучше - употребляйте ");
                plugin.setWord(l26, "Вы использовали верный код.");
                plugin.setWord(l27, "не на сервере.");
                plugin.setWord(l28, "больше нет зависимости.");
                plugin.setWord(l29, "Редактировать");
                plugin.setWord(l30, "Создание через Shift невозможно!");
                logger.info("Using russian language config.");
                break;
            case "BRAZILIAN":
                plugin.setWord(l1, "Qualidade");
                plugin.setWord(l2, "Você consumiu");
                plugin.setWord(l3, "Você não tem permissão para fazer isso!");
                plugin.setWord(l4, "Comandos do MyTrip");
                plugin.setWord(l5, "Sóbrio!");
                plugin.setWord(l6, "Você não tá doidão!");
                plugin.setWord(l7, "Esse é um comando apenas de jogadores.");
                plugin.setWord(l8, "conjunto de drogas");
                plugin.setWord(l9, "anti toxina");
                plugin.setWord(l10, "Botão direito");
                plugin.setWord(l11, "Você recebeu");
                plugin.setWord(l12, "não existe!");
                plugin.setWord(l13, "Estado");
                plugin.setWord(l15, "Não foi possível atualizar o arquivo de configuração");
                plugin.setWord(l16, "não está correto");
                plugin.setWord(l17, "drogas");
                plugin.setWord(l18, "Como criar sua droga");
                plugin.setWord(l19, "Efeitos de clique");
                plugin.setWord(l20, "Ativar efeitos fx");
                plugin.setWord(l21, "teste de droga");
                plugin.setWord(l22, "tá doidão.");
                plugin.setWord(l23, "não tá doidão.");
                plugin.setWord(l24, "Dependência de drogas");
                plugin.setWord(l25, "Para se sentir melhor ");
                plugin.setWord(l26, "Você resgatou um código correto.");
                plugin.setWord(l27, "não está no servidor.");
                plugin.setWord(l28, "não está mais viciado.");
                plugin.setWord(l29, "editar");
                plugin.setWord(l30, "Não é possível mudar os medicamentos de construção!");
                logger.info("Using brazilian language config.");
                break;
            case "DUTCH":
                plugin.setWord(l1, "Kwaliteit");
                plugin.setWord(l2, "Je verbruikte");
                plugin.setWord(l3, "Je hebt geen toestemming om dit te doen!");
                plugin.setWord(l4, "MyTrip commando's");
                plugin.setWord(l5, "Ontnuchterd!");
                plugin.setWord(l6, "Je bent niet high!");
                plugin.setWord(l7, "Dit is alleen een commando voor spelers.");
                plugin.setWord(l8, "Drugspakket");
                plugin.setWord(l9, "Tegengif");
                plugin.setWord(l10, "Klink met rechts");
                plugin.setWord(l11, "Je ontving");
                plugin.setWord(l12, "bestaat niet!");
                plugin.setWord(l13, "Status");
                plugin.setWord(l15, "Kan configuratiebestanden niet herladen");
                plugin.setWord(l16, "is niet juist");
                plugin.setWord(l17, "drugs");
                plugin.setWord(l18, "Hoe maak je jouw drugs");
                plugin.setWord(l19, "Klik op effecten");
                plugin.setWord(l20, "Activeer fx-effecten");
                plugin.setWord(l21, "Drugs test");
                plugin.setWord(l22, "is high.");
                plugin.setWord(l23, "is niet high.");
                plugin.setWord(l24, "Drugsverslaving");
                plugin.setWord(l25, "Om beter te voelen na consumptie ");
                plugin.setWord(l26, "Je hebt een juist code ingewisseld.");
                plugin.setWord(l27, "is niet op de server.");
                plugin.setWord(l28, "is niet langer verslaafd.");
                plugin.setWord(l29, "Bewerken");
                plugin.setWord(l30, "Shiften is niet mogelijk tijdens het maken van drugs!");
                logger.info("Using dutch language config.");
                break;
            case "POLISH":
                plugin.setWord(l1, "Jakość");
                plugin.setWord(l2, "Zużyłeś");
                plugin.setWord(l3, "Nie masz uprawnień, aby to zrobić!");
                plugin.setWord(l4, "Polecenia MyTrip");
                plugin.setWord(l5, "Trzeźwy!");
                plugin.setWord(l6, "Nie jesteś na fazie!");
                plugin.setWord(l7, "Jedynie gracze mogą używać tej komendy.");
                plugin.setWord(l8, "zestaw narkotyków");
                plugin.setWord(l9, "antytoksyny");
                plugin.setWord(l10, "PPM");
                plugin.setWord(l11, "Otrzymałeś");
                plugin.setWord(l12, "nie istnieje!");
                plugin.setWord(l13, "Stan");
                plugin.setWord(l15, "Nie udało się załadować ponownie plików konfiguracyjnych");
                plugin.setWord(l16, "jest nieprawidłowy");
                plugin.setWord(l17, "narkotyki");
                plugin.setWord(l18, "Jak wytworzyć swój narkotyk");
                plugin.setWord(l19, "Click at effects"); //TODO
                plugin.setWord(l20, "Aktywuj efekty fx");
                plugin.setWord(l21, "test narkotykowy");
                plugin.setWord(l22, "Jest na fazie.");
                plugin.setWord(l23, "Nie jest na fazie.");
                plugin.setWord(l24, "Uzależnienie od narkotyków");
                plugin.setWord(l25, "Aby czuć się lepiej spożytym ");
                plugin.setWord(l26, "Zrealizowałeś poprawny kod.");
                plugin.setWord(l27, "Nie jest na serwerze.");
                plugin.setWord(l28, "nie jest już uzależniony.");
                plugin.setWord(l29, "Zmień");
                plugin.setWord(l30, "Shift crafting drugs is not possible!"); //TODO
                logger.info("Using polish (BETA) language config. " + ChatColor.YELLOW + "This language is currently unfinished!");
                break;
            case "KOREAN":
                plugin.setWord(l1, "Quality"); //TODO
                plugin.setWord(l2, "You consumed"); //TODO
                plugin.setWord(l3, "You do not have the permissions to do this!"); //TODO
                plugin.setWord(l4, "MyTrip Commands"); //TODO
                plugin.setWord(l5, "Sobered up!"); //TODO
                plugin.setWord(l6, "You're not high!"); //TODO
                plugin.setWord(l7, "This is an only players command."); //TODO
                plugin.setWord(l8, "drug set"); //TODO
                plugin.setWord(l9, "anti toxin"); //TODO
                plugin.setWord(l10, "Right click"); //TODO
                plugin.setWord(l11, "You received"); //TODO
                plugin.setWord(l12, "does not exist!"); //TODO
                plugin.setWord(l13, "State"); //TODO
                plugin.setWord(l15, "설정을 불러올 수 없습니다");
                plugin.setWord(l16, "정확하지 않습니다");
                plugin.setWord(l17, "약물");
                plugin.setWord(l18, "약 제조방법");
                plugin.setWord(l19, "클릭하면 효과가 나타납니다");
                plugin.setWord(l20, "FX 효과 활성화");
                plugin.setWord(l21, "약물 태스트기");
                plugin.setWord(l22, "높다");
                plugin.setWord(l23, "높지 않습니다");
                plugin.setWord(l24, "약물 중독");
                plugin.setWord(l25, "To feel better consume "); //TODO
                plugin.setWord(l26, "You redeemed a correct code."); //TODO
                plugin.setWord(l27, "is not on the server."); //TODO
                plugin.setWord(l28, "is no longer addicted."); //TODO
                plugin.setWord(l29, "Edit"); //TODO
                plugin.setWord(l30, "Shift crafting drugs is not possible!"); //TODO
                logger.info("Using korean (BETA) language config. "  + ChatColor.YELLOW + "This language is currently unfinished!");
            case "ITALIAN":
                plugin.setWord(l1, "Qualità");
                plugin.setWord(l2, "Hai assunto");
                plugin.setWord(l3, "Non hai i permessi per farlo!");
                plugin.setWord(l4, "Comandi MyTrip");
                plugin.setWord(l5, "Sei sobrio!");
                plugin.setWord(l6, "Non sei in botta!");
                plugin.setWord(l7, "Questo è un comando per soli player.");
                plugin.setWord(l8, "Laboratorio chimico");
                plugin.setWord(l9, "Antidoto");
                plugin.setWord(l10, "Click destro");
                plugin.setWord(l11, "Hai ottenuto");
                plugin.setWord(l12, "non esiste!");
                plugin.setWord(l13, "Stato");
                plugin.setWord(l15, "Impossibile ricaricare i file di configurazione");
                plugin.setWord(l16, "non è corretto");
                plugin.setWord(l17, "droghe");
                plugin.setWord(l18, "Come sintetizzare la tua droga");
                plugin.setWord(l19, "Clicca sugli effetti");
                plugin.setWord(l20, "Attiva Effetti Speciali");
                plugin.setWord(l21, "Test tossicologico");
                plugin.setWord(l22, "è in botta.");
                plugin.setWord(l23, "non è in botta.");
                plugin.setWord(l24, "Tossicodipendenza");
                plugin.setWord(l25, "Per sentirti meglio consuma ");
                plugin.setWord(l26, "Hai riscattato un codice valido.");
                plugin.setWord(l27, "non si trova sul server.");
                plugin.setWord(l28, "non è più dipendente.");
                plugin.setWord(l29, "Modifica");
                plugin.setWord(l30, "Shift crafting drugs is not possible!"); //TODO
                logger.info("Using italian (beta) language config."  + ChatColor.YELLOW + "This language is currently unfinished!");
                break;
            default:
                logger.info("Error 010: Could not find " + lang + " config file.");
                logger.info("Please help us translating your language! Visit https://crowdin.com/project/mytrip");
                logger.info("Using custom language config.");
                break;
        }
        plugin.fc.getLanguage().options().copyDefaults(true);
        plugin.fc.saveLanguage();
    }
}