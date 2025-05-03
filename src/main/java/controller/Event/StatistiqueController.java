package controller.Event;

import entity.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import service.EventService;

public class StatistiqueController {

    @FXML
    private PieChart statusChart;

    private final EventService eventService = new EventService();

    @FXML
    private void initialize() {
        int termine = 0;
        int bientot = 0;
        int enCours = 0;

        for (Event event : eventService.getAll()) {
            String statut = event.getStatuEvent().toLowerCase();
            switch (statut) {
                case "terminé":
                    termine++;
                    break;
                case "bientôt commencé":
                    bientot++;
                    break;
                case "en cours":
                    enCours++;
                    break;
            }
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Terminé", termine),
                new PieChart.Data("Bientôt commencé", bientot),
                new PieChart.Data("En cours", enCours)
        );

        statusChart.setData(pieChartData);
        statusChart.setTitle("Répartition des événements par statut");
    }


}
