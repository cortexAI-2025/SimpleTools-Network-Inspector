# SimpleTools Network Inspector — by AIWORKPAY

Application Android gratuite pour diagnostiquer les connexions Wi-Fi, le réseau local et l’accès Internet.

## Statut V1.0.0 (en cours)

### Implémenté
- Structure Gradle, CI GitHub Actions, permissions minimales.
- NetworkInfoRepository (ConnectivityManager, LinkProperties, VPN, DNS, MTU).
- WifiInfoRepository (SSID, RSSI, fréquence, vitesse de liaison).
- DnsRepository (résolution système IPv4/IPv6).
- PingRepository : test TCP Connect explicite (jamais présenté comme ICMP).
- AutomaticDiagnostic : diagnostic en un clic (réseau, Wi-Fi, passerelle, DNS).
- SecurityCheck : observations défensives sans score absolu.
- LAN Discovery : confirmation explicite obligatoire, plage limitée au sous-réseau privé, timeouts, pas de scan Internet, absence de réponse ≠ équipement éteint.
- DashboardViewModel + écran d’accueil avec bouton « Analyser ma connexion ».
- Entité Room DiagnosticEntity pour l’historique local.
- Test unitaire de base (DnsRepository).

### À faire
- Graphiques (RSSI, latence).
- Exports PDF / CSV / JSON avec masquage optionnel.
- Historique complet (consultation, suppression, comparaison).
- Tests automatisés étendus et recette Galaxy A14.

## Principes

- 100 % gratuit, sans publicité, sans compte, sans télémétrie, sans backend.
- Historique local uniquement.
- Diagnostics réels via API Android officielles. Aucune simulation présentée comme réelle.

Package : `com.simpletools.networkinspector`  
Min SDK : 29  
Version : 1.0.0 (versionCode 1)
