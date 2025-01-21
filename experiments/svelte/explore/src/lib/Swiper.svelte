<script>
    import {swiperAction} from "./swiperAction.svelte.js";
    let next = $state(null);
    let prev = $state(null);
    let pagination = $state(null);
</script>

<div use:swiperAction={{
    spaceBetween: 30,
    centeredSlides: true,
    autoplay: {
        delay: 2500,
        disableOnInteraction: false,
    },
    pagination: {
        el: pagination,
        clickable: true,
    },
    navigation: {
        nextEl: next,
        prevEl: prev,
    },
  }} class="swiper">
    <div class="swiper-wrapper">
        <div class="swiper-slide">
            <img src="https://swiperjs.com/demos/images/nature-1.jpg" />
          </div>
          <div class="swiper-slide">
            <img src="https://swiperjs.com/demos/images/nature-2.jpg" />
          </div>
          <div class="swiper-slide">
            <img src="https://swiperjs.com/demos/images/nature-3.jpg" />
          </div>
          <div class="swiper-slide">
            <img src="https://swiperjs.com/demos/images/nature-4.jpg" />
          </div>
    </div>
    <div bind:this={next} class="swiper-button-next"></div>
    <div bind:this={prev} class="swiper-button-prev"></div>
    <div bind:this={pagination} class="swiper-pagination"></div>
</div>
