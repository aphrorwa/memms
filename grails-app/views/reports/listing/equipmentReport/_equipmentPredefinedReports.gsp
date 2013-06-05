<script type='text/javascript'>
$(document).ready(function(){
    var items, scroller = $('.v-tabs-subnav');
    var width = 0;
    var item = 0;
    var scrolledWidth = 0;
    scroller.children().each(function(){
        width += $(this).outerWidth(true);
    });
    scroller.css('width', width);

    $(document).on('click', '.v-tabs-subnav-scroll-left', function(e){
      e.preventDefault();
      items = scroller.children();
      if(item > 0) {
        scrollRight(item);
        item -= 5;
      }
    });

    $(document).on('click', '.v-tabs-subnav-scroll-right', function(e){
      e.preventDefault();
      items = scroller.children();
      if(item < (items.length-6)){
        scrollLeft(item);
        item += 5;
      }
    });

    function scrollLeft(item){
      var scrollWidth = 0
      items.each(function(idx){
        if(idx < 5){
          scrollWidth += $(items[idx]).outerWidth();
        }
      });
      scrolledWidth += scrollWidth;
      scroller.animate({'left' : (0 - scrolledWidth) + 'px'}, 'linear');
    }

    function scrollRight(item){
      var scrollWidth = 0
      items.each(function(idx){
        if(idx < 5){
          scrollWidth += $(items[idx]).outerWidth();
        }
      });
      scrolledWidth -= scrollWidth;
      scroller.animate({'left' : (0 - scrolledWidth) + 'px'}, 'linear');
    }

    $(document).on('click', '#js-delete-node', function(e) {
      e.preventDefault();
      if(window.confirm("Are you sure you want to delete this report?")){
        $(this).parents('li').remove();
        /* AJAX CALL HERE */
      }
    });
});
</script>
<div id='js-chosen-report' class='v-tabs-chosen-report'>
  <!-- Generate selected report name with Groovy -->
  <a href='#' class='active'>Obsolete Equipments</a>
</div>
<a class='v-tabs-subnav-scroll-left' href='#' id='js-scroll-left'><</a>
<a class='v-tabs-subnav-scroll-right' href='#' id='js-scroll-right'>></a>
<div class='v-tabs-subnav-wrapper slide-wrapper' id='#js-slider-wrapper'>
  <ul class="v-tabs-subnav slide">
    <li>
      <a href="${createLinkWithTargetURI(controller: 'listing', action:'generalEquipmentsListing')}" id="report-1">
              <g:message code="default.all.equipments.label" />
      </a>
      <span class='delete-node' id='js-delete-node'>X</span>
    </li>
    <li>
      <a href="${createLinkWithTargetURI(controller: 'listing', action:'obsoleteEquipments')}" id="report-2">
              <g:message code="default.obsolete.label" />
      </a>
      <span class='delete-node' id='js-delete-node'>X</span>
    </li>
    <li>
      <a href="${createLinkWithTargetURI(controller: 'listing', action:'disposedEquipments')}" id="report-3">
              <g:message code="default.disposed.label" />
      </a>
      <span class='delete-node' id='js-delete-node'>X</span>
    </li>
    <li>
      <a href="${createLinkWithTargetURI(controller: 'listing', action:'underMaintenanceEquipments')}" id="report-4">
              <g:message code="default.under.maintenance.label" />
      </a>
    </li>
    <li>
      <a href="${createLinkWithTargetURI(controller: 'listing', action:'inStockEquipments')}" id="report-5">
              <g:message code="default.in.stock.label" />
      </a>
    </li>
    <li>
      <a href="${createLinkWithTargetURI(controller: 'listing', action:'inStockEquipments')}" id="report-5">
              <g:message code="default.in.stock.label" />
      </a>
    </li>
    <li>
      <a href="${createLinkWithTargetURI(controller: 'listing', action:'inStockEquipments')}" id="report-5">
              <g:message code="default.in.stock.label" />
      </a>
    </li>
    <li>
      <a href="${createLinkWithTargetURI(controller: 'listing', action:'inStockEquipments')}" id="report-5">
              <g:message code="default.in.stock.label" />
      </a>
    </li>
    <li>
      <a href="${createLinkWithTargetURI(controller: 'listing', action:'inStockEquipments')}" id="report-5">
              <g:message code="default.in.stock.label" />
      </a>
    </li>
    <li>
      <a href="${createLinkWithTargetURI(controller: 'listing', action:'inStockEquipments')}" id="report-5">
              <g:message code="default.in.stock.label" />
      </a>
    </li>
    <li>
      <a href="${createLinkWithTargetURI(controller: 'listing', action:'inStockEquipments')}" id="report-5">
              <g:message code="default.in.stock.label" />
      </a>
    </li>
    <li>
      <a href="${createLinkWithTargetURI(controller: 'listing', action:'inStockEquipments')}" id="report-5">
              <g:message code="default.in.stock.label" />
      </a>
    </li>
    <li>
      <a href="${createLinkWithTargetURI(controller: 'listing', action:'inStockEquipments')}" id="report-5">
              <g:message code="default.in.stock.label" />
      </a>
    </li>
  </ul>
</div>
