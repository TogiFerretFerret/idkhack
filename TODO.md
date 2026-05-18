# idkhack - Porting TODO

## Disabled Modules (need 1.21.11 port)

These modules are stubbed out and need to be rewritten for MC 1.21.11 API changes.

### Combat
- **AutoWeb** - web trap placement
- ~~**AutoXP**~~ - done
- ~~**Criticals**~~ - done

### Movement
- **EntitySpeed** - speed on rideable entities
- **PhaseWalk** - walk through blocks

### Render
- **Chams** - entity wallhack rendering
- **LogoutSpots** - show where players logged out
- **Nametags** - custom nametag rendering
- **PopChams** - totem pop visualization
- **Shaders** - shader-based ESP/glow effects
- **Skeleton** - wireframe player skeleton overlay (partially working)
- ~~**TimeChanger**~~ - done
- **Tooltips** - enhanced shulker/item tooltips
- **Waypoints** - in-world waypoint rendering

### Player
- **AutoLog** - auto disconnect on danger

### Misc
- ~~**AutoRespawn**~~ - done
- **Crafter** - auto crafting
- **Sense** - anti-cheat visualization

## Common API Changes (1.21.1 -> 1.21.11)

- `Entity.getPos()` removed -> use `new Vec3d(entity.getX(), entity.getY(), entity.getZ())`
- `DrawContext.getMatrices()` returns `Matrix3x2fStack` not `MatrixStack`
- `PlayerMoveC2SPacket` constructors need extra `boolean horizontalCollision` param
- `PlayerPositionLookS2CPacket` is now a record with `.teleportId()`, `.change().position()`
- `RenderSystem.setShader()`, `setShaderColor()`, `BufferRenderer.drawWithGlobalProgram()` removed
- Entity rendering uses render states instead of direct entity references
- `ArmorItem` replaced by `EquippableComponent` from data components
- `KeyBinding.matchesKey()` now takes `KeyInput` instead of `(int, int)`
- `Framebuffer` is now abstract

## New Features Planned
- MapDownloader
- StashFinder
- EntityLogger
- AccountSwitcher
- SignSearch
- ShulkerViewer (enhanced)
- SignBot / BookBot
- ViaFabricPlus integration
