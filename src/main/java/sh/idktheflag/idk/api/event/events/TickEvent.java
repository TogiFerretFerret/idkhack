package sh.idktheflag.idk.api.event.events;

import sh.idktheflag.idk.api.event.Event;

public class TickEvent {
    /**
     * @see sh.idktheflag.idk.mixin.MixinMinecraftClient
     */
    public static class ClientTickEvent extends Event {
    }
    /**
     * @see sh.idktheflag.idk.mixin.MixinMinecraftClient
     */
    public static class AfterClientTickEvent extends Event {
    }

    /**
     * @see sh.idktheflag.idk.mixin.MixinMinecraftClient
     */
    public static class InputTick extends Event {

    }


    /**
     * @see sh.idktheflag.idk.mixin.MixinMinecraftClient
     */
    public static class GameRenderTick extends Event {

    }



    /**
     * @see sh.idktheflag.idk.mixin.MixinMinecraftClient
     */
    public static class VanillaTick extends Event {

    }


    /**
     * @see sh.idktheflag.idk.mixin.MixinClientPlayerEntity
     */
    public static class MovementTickEvent extends Event {


        public static class Pre extends Event {

            public Pre()
            {
            }
        }


        public static class Post extends Event {

            public Post()
            {

            }
        }
    }

    /**
     * @see sh.idktheflag.idk.mixin.MixinClientPlayerEntity
     */
    public static class PlayerTickEvent extends Event {
        public static class Pre extends Event {

            public Pre()
            {

            }
        }


        public static class Post extends Event {

            public Post()
            {

            }
        }
    }

}
