# RusherCrack Reverse Engineering Devlog

### Hour 1: Extracting the Artifacts
Decided to take a break from porting to 1.21.11 and instead started looking into how RusherHack handles its obfuscation. First step was extracting `rushercrack-2.0.jar`. It looks like it bundles the actual modules inside the `org/rusherhack` namespace, but they are all heavily obfuscated with weird unicode names (like `─│.class`, `qo.class`, `zn.class`). It also bundles a `pluto/solutions/RusherCrack.class` which acts as the entrypoint to extract the main menu shaders. That part was pretty easy to read.

### Hour 2: Decompiling the Core
I pulled in CFR and started throwing some of the larger classes at it. `A.class` decompiled into an Enum that looks like it wraps `net.minecraft.class_124` (which is `Formatting` in Yarn mappings). The crazy part is that every string is decrypted at runtime using `java.lang.invoke.CallSite`. The strings aren't stored in the constant pool in plain text, making static analysis a huge pain.

### Hour 3: Analyzing `zn.class`
Ran CFR on `zn.class` (which is almost 900kb compiled!). It's definitely a core utility class for string decryption and maybe HWID checks. It uses `MessageDigest`, `ConcurrentHashMap`, and `invokedynamic` everywhere. All the string literals are obfuscated and resolved dynamically via `MethodHandles`. I'm starting to write a small Java agent to hook into these `CallSite` bootstraps to dump the decrypted strings at runtime, otherwise there's no way I'm manually reversing 900kb of switch statements and bitwise XORs.

### Hour 4: Mapping Obfuscated Methods
Still grinding away at the `org/rusherhack` classes. I found a few classes that extend `Module` and implement `TickEvent` listeners, but their names are literally box-drawing characters. I'm writing a Python script to parse the CFR output and rename these classes to something readable based on their superclasses and the events they subscribe to. The `CallSite` decryption is standard ZKM (Zelix KlassMaster) stuff, but they layered it with custom string encryption. Might need to use a tool like Java Deobfuscator instead of plain CFR if I want clean source.
