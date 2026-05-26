# 🔐 AES Cipher — Cifrado Simétrico AES-256-GCM

> **Cifra y descifra mensajes con la fuerza del estándar AES-256-GCM en una interfaz limpia, rápida y sin dependencias externas.**

---

## 🎯 Objetivo

**AES Cipher** es una aplicación de escritorio de código abierto diseñada para cifrar y descifrar textos utilizando el algoritmo **AES-256-GCM**, el estándar de oro en cifrado simétrico recomendado por el NIST y utilizado por gobiernos, bancos y empresas de todo el mundo.

¿Por qué existe este proyecto? Porque **la privacidad no debería ser complicada**. Si necesitas enviar un mensaje seguro, proteger un documento o simplemente experimentar con criptografía moderna, AES Cipher te lo pone fácil: sin servidores, sin cuentas, sin conexión a internet. **Todo ocurre en tu máquina.**

---
### 📥 Descargar Ejecutable

Haz clic en el siguiente enlace para obtener la versión estable más reciente:

👉 **[Descargar Última Versión (.JAR)](https://github.com)**

---

### 🛠️ Requisitos y Cómo Ejecutar

Para ejecutar esta aplicación en tu computadora, necesitas tener instalado **Java (JRE/JDK) versión 21** o superior.

**Instrucciones de uso:**
1. Descarga el archivo `.jar` desde el enlace de arriba.
2. Abre tu terminal o consola de comandos.
3. Navega hasta la carpeta de descarga y ejecuta:
   ```bash
   java -jar tu-archivo-app.jar
   ```
*(Nota: En muchos sistemas operativos como Windows o macOS, también puedes ejecutarlo haciendo **doble clic** sobre el archivo `.jar` si ya tienes Java bien configurado).*

## ✨ Funcionalidades

| Función | Descripción |
|---------|-------------|
| 🔒 **Cifrado AES-256-GCM** | Cifra cualquier texto con una clave generada aleatoriamente |
| 🔓 **Descifrado seguro** | Recupera el mensaje original con la misma clave y IV |
| ⚡ **Generador de claves** | Crea claves AES-256 y IV (Vector de Inicialización) en Base64 |
| 📋 **Copiado rápido** | Un clic para copiar claves, IV o texto cifrado al portapapeles |
| 🔄 **Autocompletado inteligente** | Transfiere automáticamente la clave y el IV entre paneles |
| 🗑️ **Limpieza total** | Borra todos los campos con un solo clic |
| 🌙 **Tema oscuro moderno** | Interfaz FlatLaf Dark con colores cuidadosamente seleccionados |
| 📏 **Contador de caracteres** | Límite de 500 caracteres con alertas visuales de color |

---

## 🏗️ Arquitectura

El proyecto sigue principios de **Arquitectura Hexagonal (Ports & Adapters)** y **Clean Architecture**, separando claramente las responsabilidades:

```
┌─────────────────────────────────────────────────────────┐
│              [ Capa de Infraestructura ]                │
│   ┌──────────────┐   ┌──────────────┐   ┌───────────┐  │
│   │   Swing UI   │   │   FlatLaf    │   │   AES     │  │
│   │   (Adapters) │   │   (Tema)     │   │   Codec   │  │
│   └──────┬───────┘   └──────────────┘   └────┬──────┘  │
│          │                                     │        │
│   ┌──────▼────────────────────────────────────▼──────┐  │
│   │              [ Capa de Dominio ]                  │  │
│   │              (AesCipher Core)                     │  │
│   └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

### Separación de capas

- **Dominio (`crypto/`)**: Lógica de cifrado pura. Sin dependencias de frameworks. Java puro con `javax.crypto`.
- **UI (`ui/`)**: Interfaz gráfica con Swing y FlatLaf. Separada en `MainFrame`, `CipherPanel` y `DecipherPanel`.
- **Utilidades (`ui/components/`)**: Componentes reutilizables como `EmojiLabel` para soporte de emojis en Windows.

### Tecnologías y Frameworks

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 17+ | Lenguaje principal |
| **Spring Boot** | 3.x | Dependencias y gestión |
| **Swing** | JDK | Interfaz gráfica de usuario |
| **FlatLaf** | 3.x | Tema Dark moderno |
| **Maven** | 3.9+ | Compilación y empaquetado |
| **javax.crypto** | JDK | Implementación AES-256-GCM |

---

## 🔬 Mecanismo de Cifrado

AES Cipher utiliza el modo **GCM (Galois/Counter Mode)** de AES-256, que proporciona:

### ¿Cómo funciona?

```
Texto Plano → [AES-256-GCM] → Ciphertext (Base64)
                  ↑
          Clave (256 bits) + IV (128 bits)
```

1. **Generación de claves**: Se genera una clave criptográfica de **256 bits (32 bytes)** y un IV de **128 bits (16 bytes)** usando `SecureRandom`.
2. **Cifrado**: El texto se cifra con AES-GCM, que combina **confidencialidad** (cifrado) y **integridad** (autenticación GCM) en un solo paso.
3. **Codificación**: La clave, el IV y el resultado se codifican en **Base64** para facilitar su manejo y transferencia.
4. **Descifrado**: Con la misma clave y IV, el ciphertext se transforma de vuelta al texto original.

### ¿Por qué AES-GCM?

| Característica | Beneficio |
|----------------|-----------|
| **Confidencialidad** | Solo quien tenga la clave puede leer el mensaje |
| **Integridad** | Detecta cualquier modificación del ciphertext |
| **Eficiencia** | Cifrado y autenticación en un solo paso |
| **Estándar** | Recomendado por NIST, FIPS 140-2, y organismos gubernamentales |

### Beneficios para el público

- 🔒 **Privacidad real**: Tus datos nunca salen de tu ordenador.
- ⚡ **Velocidad**: Cifrado y descifrado instantáneos.
- 🛡️ **Seguridad de nivel militar**: AES-256 es considerado inquebrantable con la tecnología actual.
- 📦 **Portabilidad**: Un único archivo `.jar` que funciona en Windows, macOS y Linux.
- 🎓 **Educación**: Código abierto para estudiar y aprender criptografía aplicada.

---

## 🚀 Instalación y Uso

### Requisitos

- Java Runtime Environment (JRE) 17 o superior

### Ejecución

```bash
# Descargar el archivo JAR desde la sección Releases
java -jar aes-cipher-app-1.0.0.jar
```

### Compilar desde fuente

```bash
git clone https://github.com/tu-usuario/aes-cipher.git
cd aes-cipher
mvn clean package
java -jar target/aes-cipher-app-1.0.0.jar
```

---

## 📂 Estructura del Proyecto

```
aes-cipher/
├── pom.xml                          # Configuración Maven
├── src/main/java/com/cypher/aes/
│   ├── App.java                     # Punto de entrada
│   ├── crypto/
│   │   └── AesCipher.java           # Lógica de cifrado (Dominio)
│   └── ui/
│       ├── MainFrame.java           # Ventana principal
│       └── components/
│           ├── CipherPanel.java     # Panel de cifrado
│           ├── DecipherPanel.java   # Panel de descifrado
│           └── EmojiLabel.java      # Utilidad de emojis
└── target/
    └── aes-cipher-app-1.0.0.jar    # Ejecutable
```

---

## 🤝 Contribuir

Aunque este es un proyecto personal, las ideas y sugerencias son bienvenidas. Si encuentras un bug o tienes una mejora en mente, no dudes en abrir un issue.

---

## ⚠️ Aviso Legal

> **Este software se proporciona "tal cual", sin garantías de ningún tipo.**
> El autor no se hace responsable del uso que se le dé. Utiliza el cifrado bajo tu propia responsabilidad.

---

## © Derechos de Autor

**Desarrollado por Moisés Martinez Mateu — 2026**

Todo el código fuente, diseño, documentación y contenido de este repositorio es propiedad exclusiva de **Moisés Martinez Mateu**. Quedan reservados todos los derechos.

**Prohibida la reproducción, distribución, modificación o uso comercial sin autorización expresa del autor.**

---

*Hecho con ☕ Java y pasión por la seguridad digital.*
