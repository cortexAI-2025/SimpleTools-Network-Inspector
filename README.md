# SimpleTools Network Inspector — by AIWORKPAY

Application Android gratuite pour diagnostiquer les connexions Wi-Fi, le réseau local et l’accès Internet.

## Statut V1.0.0 (en cours)

### Implémenté
- Structure Gradle, CI GitHub Actions, permissions minimales.
- NetworkInfoRepository, WifiInfoRepository, DnsRepository.
- PingRepository (TCP Connect explicite).
- AutomaticDiagnostic (un clic).
- SecurityCheck (observations défensives).
- LAN Discovery (confirmation obligatoire, limites strictes).
- Historique local complet (Room + écran).
- Exports CSV / JSON / PDF.
- Helper de série pour graphiques.
- Navigation multi-écrans (Dashboard, Historique, LAN Discovery, Paramètres).
- Écran Paramètres (thème, confidentialité, version).
- Test unitaire de base.

### À faire
- Rendu graphique Compose (RSSI, latence).
- Masquage optionnel des données sensibles avant export.
- Écrans dédiés Wi-Fi Analyzer et Ping Monitor.
- Tests automatisés étendus.
- Recette sur Samsung Galaxy A14.

## Principes

- 100 % gratuit, sans publicité, sans compte, sans télémétrie, sans backend.
- Historique local uniquement.
- Diagnostics réels via API Android officielles. Aucune simulation présentée comme réelle.

Package : `com.simpletools.networkinspector`  
Min SDK : 29  
Version : 1.0.0 (versionCode 1)
