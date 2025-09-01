# ObsiModules

App Android (Kotlin + Jetpack Compose + API 35) para leer y editar módulos en texto plano/Markdown al estilo Obsidian.

## Estructura
- Un archivo `.md` o `.txt` = un **módulo**.
- `#` título del módulo, `##` submódulos.

## Compilación rápida
```bash
./gradlew assembleDebug
# APK en app/build/outputs/apk/debug/app-debug.apk
```
## CI (GitHub Actions)

Este repo incluye `.github/workflows/android.yml` que:

- Compila el APK **Debug** en cada push/PR.
- Sube el APK como **artifact** descargable en la sección de Actions.
- Si configuras los secretos de firma (`SIGNING_KEYSTORE_BASE64`, `SIGNING_STORE_PASSWORD`, `SIGNING_KEY_ALIAS`, `SIGNING_KEY_PASSWORD`) puedes ejecutar manualmente (`workflow_dispatch`) el job **release** para obtener un APK firmado listo para publicar.
