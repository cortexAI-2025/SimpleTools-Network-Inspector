# SimpleTools Network Inspector — by AIWORKPAY

Application Android gratuite pour diagnostiquer les connexions Wi-Fi, le réseau local et l’accès Internet.

## Statut V1.0.0 (en cours)

### Implémenté
- Structure Gradle, CI GitHub Actions, permissions minimales.
- NetworkInfoRepository (ConnectivityManager, LinkProperties, VPN, DNS, MTU).
- WifiInfoRepository (SSID, RSSI, fréquence, vitesse de liaison).
- DnsRepository (résolution système IPv4/IPv6).
- DashboardViewModel + écran d’accueil affichant les données réelles.
- Entité Room DiagnosticEntity pour l’historique local.
- Test unitaire de base (DnsRepository).

### À faire
- Ping Monitor (TCP Connect fallback honnête).
- Diagnostic automatique en un clic.
- LAN Discovery (avec confirmation et limites).
- Security Check défensif.
- Graphiques, exports PDF/CSV/JSON, historique complet.
- Tests automatisés étendus.

## Principes

- 100 % gratuit, sans publicité, sans compte, sans télémétrie, sans backend.
- Historique local uniquement.
- Diagnostics réels via API Android officielles. Aucune simulation présentée comme réelle.

Package : `com.simpletools.networkinspector`  
Min SDK : 29  
Version : 1.0.0 (versionCode 1)
