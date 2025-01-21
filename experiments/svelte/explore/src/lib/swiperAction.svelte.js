import Swiper from 'swiper';
import 'swiper/css/bundle';

export function swiperAction(node, options) {

    $effect(() => {
        let swiper = new Swiper(node, {
            ...options
        });
        return () => {
            swiper.destroy();
            swiper = null;
        }
    })
}