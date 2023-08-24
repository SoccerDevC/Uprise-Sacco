<x-layout bodyClass="g-sidenav-show  bg-gray-200">
<main class="main-content position-relative max-height-vh-100 h-100 border-radius-lg ">
<x-navbars.navs.auth titlePage="Report"></x-navbars.navs.auth>
<div class="container-fluid py-4">
    <html>
    <div class="row">
        <div class="col-12">

            <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
                <div class="bg-dark shadow-primary border-radius-lg pt-4 pb-3">
                    <div class="alert alert-warning alert-dismissible text-white">
                        <h3 class="text-white text-center">
                            UPRISE SACCO
                        </h3>
                        <h6 class=" text-center" style="font-style:italic; color:black">
                            Saving together, thriving together
                        </h6>
                    </div>
                </div>

            </div>
        </div>
        <!-- ------------------------------------ -->

        <!-- ===========================CHARTS====================================== -->

        <div class="row mt-4">
            <div class="col-lg-3 col-md-6 mt-4 mb-4">

                <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                    <div class="bg-success shadow-primary border-radius-lg py-3 pe-1">
                        <div class="chart">
                            <div class="card-body text-center">
                                <h6 class="mb-0 text-blue"> Members </h6>

                                <p class="text-white display-4 my-4"> <span class="font-weight-bolder">{{ $members->count() }}</span> </p>
                                <hr class="dark horizontal">
                                <div class="d-flex ">
                                    <i class="material-icons text-sm my-auto me-1 text-white">schedule</i>
                                    <p class="mb-0 text-sm text-white">
                                        Updated:

                                        @if ($members->count() > 0)
                                        {{ $members->max('updated_at')->diffForHumans() }} @else
                                        No updates yet
                                        @endif

                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>


            </div>
            <div class="col-lg-6 col-md-8 mt-4 mb-4">

                <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                    <div class="bg-gradient-secondary shadow-success border-radius-lg py-3 pe-1">
                        <div class="chart">
                            <div class="card-body text-center">
                                <h6 class="mb-0 text-blue">Members' Total Contributions </h6>
                                @php
                                $totalContributions = $members->sum('total_contributions');
                                @endphp
                                <p class="text-white display-4 my-4"> <span class="font-weight-bolder">{{ $totalContributions }}</span> </p>
                                <hr class="dark horizontal">
                                <div class="d-flex ">
                                    <i class="material-icons text-sm my-auto me-1 text-white">schedule</i>
                                    <p class="mb-0 text-sm text-white">
                                        Updated:
                                        @if ($members->count() > 0)
                                        {{ $members->max('updated_at')->diffForHumans() }} @else
                                        No updates yet
                                        @endif
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>




            </div>
            <div class="col-lg-3 mt-4 mb-3">

                <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                    <div class="bg-success shadow-dark border-radius-lg py-3 pe-1">


                        <div class="chart">
                            <div class="card-body text-center">
                                <h6 class="mb-0 text-blue"> Registered Loans </h6>

                                <p class="text-white display-4 my-4"> <span class="font-weight-bolder">{{ $registeredLoans->count() }}</span> </p>
                                <hr class="dark horizontal">
                                <div class="d-flex">
                                    <i class="material-icons text-sm my-auto me-1 text-white">schedule</i>
                                    <p class="mb-0 text-sm text-white">
                                        Recently
                                    </p>
                                </div>
                            </div>
                        </div>

                    </div>


                </div>
            </div>
        </div>

    </html>
</div>
</main>
</x-layout>