# idk - Porting TODO

## Disabled Modules (need 1.21.11 port)

These modules are stubbed out and need to be rewritten for MC 1.21.11 API changes.

### Combat
- **AutoWeb** - web trap placement (fixed)
- ~~**AutoXP**~~ - done
- ~~**AutoAnchor**~~ - done
- ~~**AutoCrystal**~~ - done
- ~~**KillAura**~~ - done
- **SelfFill** - burrow (fixed)
- **Surround** - feet protection (fixed)

### Movement
- **EntitySpeed** - speed on rideable entities (fixed)
- **PhaseWalk** - walk through blocks (fixed)
- **Step** - (fixed)
- **LongJump** - (fixed)
- **AutoWalk** - (fixed)
- **NoSlow** - (fixed)
- **PacketFly** - (fixed - stole better record handling logic)

### Render
- **LogoutSpots** - show where players logged out (fixed)
- **Nametags** - custom nametag rendering (partially done)
- **PopChams** - totem pop visualization (fixed)
- **Tooltips** - shulker tooltips (fixed)
- **Waypoints** - render saved waypoints (fixed)
- **StashFinder** - (fixed)
- **SignSearch** - (fixed)
- **Shaders** - post-processing effects
- **Skeleton** - (partially working)

## Technical Debt / API Changes
- `MatrixStack` is often replaced by `DrawContext` in GUI code
- `DrawContext` is now used for almost all UI rendering
- `Packet` sending/receiving changed for some classes (e.g. `PlayerMoveC2SPacket`)
- `PlayerInventory` field access changed
- `Entity.getYaw()`/`getPitch()` still work, but `prevYaw` is now `lastYaw`
- `Text.of()` or `Text.literal()` instead of `new LiteralText()`
- `Screen` init now uses `init()` instead of `init(MinecraftClient, int, int)`
- `ButtonWidget` uses `builder()`
- `SlotActionType` is still the same but `clickSlot` parameters changed
- `Identifier` creation changed to `Identifier.of()`
- `DrawContext.drawText` requires `DrawContext` instead of `(int, int)`
- `Framebuffer` is now abstract

## New Features Planned
- MapDownloader (done)
- StashFinder (done)
- EntityLogger (done)
- AccountSwitcher (done)
- SignSearch (done)
- ShulkerViewer (enhanced)
- SignBot (done)
- BookBot (done)
- ViaFabricPlus integration (done)
