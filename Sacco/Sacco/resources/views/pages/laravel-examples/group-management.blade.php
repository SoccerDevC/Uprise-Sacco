<x-layout bodyClass="g-sidenav-show  bg-gray-200">

    <x-navbars.sidebar activePage="group-management"></x-navbars.sidebar>
    <main class="main-content position-relative max-height-vh-100 h-100 border-radius-lg ">
        <!-- Navbar -->
        <x-navbars.navs.auth titlePage="Group Management"></x-navbars.navs.auth>
        <!-- End Navbar -->
        x<div class="container-fluid py-4">
                <div class="row">
                    <div class="col-12">
                        <div class="card my-4">
                            <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
                                <div class="alert alert-warning alert-dismissible shadow-primary border-radius-lg pt-4 pb-3">
                                    <h6 class="text-white text-capitalize ps-3">G-6 Group Members</h6>
                                </div>
                            </div>
                            <div class="card-body px-0 pb-2">
                                <div class="table-responsive p-0">
                                    {{-- <div class="alert alert-warning alert-dismissible shadow-primary border-radius-lg pt-4 pb-3"> --}}
                                    <table class="table align-items-center mb-0">
                                        <thead>
                                            
                                            <tr>
                                               
                                                <th
                                                    class="text-uppercase text-black text-secondary text-xxs font-weight-bolder opacity-7" style="font-color:black">
                                                    Name</th>
                                                    <th
                                                    class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">
                                                   email address</th>    
                                                <th
                                                    class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">
                                                    Reg. Number</th>
                                                <th
                                                    class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">
                                                   status</th>
                                                <th
                                                    class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">
                                                    student no.</th>
                                                <th class="text-secondary opacity-7"></th>
                                                
                                            </tr>
                                        
                                        </thead>
                                    </div>
                                        <tbody>
                                            
                                            <tr>
                                                <td>
                                                    <div class="d-flex px-2 py-1">
                                                        
                                                        {{-- <div>
                                                            <img src="{{ asset('assets') }}/img/team-2.jpg"
                                                                class="avatar avatar-sm me-3 border-radius-lg"
                                                                alt="user1">
                                                        </div> --}}
                                                        <div class="d-flex flex-column justify-content-center">
                                                            <h6 class="mb-0 text-sm">TUKWASIIBWE MARTIN</h6>
                                                            {{-- <p class="text-xs text-secondary mb-0">siibwemart@gmail.com
                                                            </p> --}}
                                                        </div>
                                                    
                                                    </div>
                                                </td>
                                                <td>
                                                    <p class="text-center text-xs font-weight-bold mb-0">siibwemart@gmail.com</p>
                                                    <p class="text-xs text-secondary mb-0"></p>
                                                </td>
                                                <td>
                                                    <p class="text-center text-xs font-weight-bold mb-0">22/U/21816/EVE</p>
                                                    <p class="text-xs text-secondary mb-0"></p>
                                                </td>
                                                <td class="align-middle text-center text-sm">
                                                    <span class="badge badge-sm bg-gradient-secondary">Online</span>
                                                </td>
                                                <td class="align-middle text-center">
                                                    <span
                                                        class="text-secondary text-xs font-weight-bold">2200721816</span>
                                                        
                                                </td>
                                                
                                                <td class="align-middle">
                                                    {{-- <a href="javascript:;"
                                                        class="text-secondary font-weight-bold text-xs"
                                                        data-toggle="tooltip" data-original-title="Edit user">
                                                        {{-- Edit 
                                                    </a> --}}
                                                    

                                                </td>
                                            
                                            </tr>
                                            
                                            <tr>
                                                
                                                <td>
                                                    <div class="d-flex px-2 py-1">
                                                         {{-- <div>
                                                            <img src="{{ asset('assets') }}/img/Melv.jpg"
                                                                class="avatar avatar-sm me-3 border-radius-lg"
                                                                alt="user2"> 
                                                        </div> --}}
                                                        <div class="d-flex flex-column justify-content-center">
                                                            <h6 class="mb-0 text-sm">MAWEJJE MELVIN NATHAN</h6>
                                                            {{-- <p class="text-xs text-secondary mb-0">
                                                                alexa@creative-tim.com</p> --}}
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>
                                                    <p class="text-center text-xs font-weight-bold mb-0">melvinmawejje@gmail.com</p>
                                                    <p class="text-xs text-secondary mb-0"></p>
                                                </td>
                                                <td>
                                                    <p class="text-center text-xs font-weight-bold mb-0">21/U/10242/EVE</p>
                                                    <p class="text-xs text-secondary mb-0"></p>
                                                </td>
                                                <td class="align-middle text-center text-sm">
                                                    <span class="badge badge-sm bg-gradient-success">Online</span>
                                                </td>
                                                <td class="align-middle text-center">
                                                    <span
                                                        class="text-secondary text-xs font-weight-bold">2100710242</span>
                                                </td>
                                                {{-- <td class="align-middle">
                                                    <a href="javascript:;"
                                                        class="text-secondary font-weight-bold text-xs"
                                                        data-toggle="tooltip" data-original-title="Edit user">
                                                        Edit
                                                    </a>
                                                </td> --}}
                                            </tr>
                                            <tr>
                                                <td>
                                                    <div class="d-flex px-2 py-1">
                                                        {{-- <div>
                                                            <img src="{{ asset('assets') }}/img/Conrad1.jpg"
                                                                class="avatar avatar-sm me-3 border-radius-lg"
                                                                alt="user3">
                                                        </div> --}}
                                                        <div class="d-flex flex-column justify-content-center">
                                                            <h6 class="mb-0 text-sm">SERUMAGA CONRAD DAVID</h6>
                                                            {{-- <p class="text-xs text-secondary mb-0">
                                                                laurent@creative-tim.com</p> --}}
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>
                                                    <p class="text-center text-xs font-weight-bold mb-0">cddavid2001@gmail.com</p>
                                                    <p class="text-xs text-secondary mb-0"></p>
                                                </td>
                                                <td>
                                                    <p class="text-center text-xs font-weight-bold mb-0">21/U/05874/PS</p>
                                                    <p class="text-xs text-secondary mb-0"></p>
                                                </td>
                                                <td class="align-middle text-center text-sm">
                                                    <span class="badge badge-sm bg-gradient-success">Online</span>
                                                </td>
                                                <td class="align-middle text-center">
                                                    <span
                                                        class="text-secondary text-xs font-weight-bold">2100705874</span>
                                                </td>
                                                {{-- <td class="align-middle">
                                                    <a href="javascript:;"
                                                        class="text-secondary font-weight-bold text-xs"
                                                        data-toggle="tooltip" data-original-title="Edit user">
                                                        Edit
                                                    </a>
                                                </td> --}}
                                            </tr>
                                            <tr>
                                                <td>
                                                    <div class="d-flex px-2 py-1">
                                                        {{-- <div>
                                                            <img src="{{ asset('assets') }}/img/team-3.jpg"
                                                                class="avatar avatar-sm me-3 border-radius-lg"
                                                                alt="user4">
                                                        </div> --}}
                                                        <div class="d-flex flex-column justify-content-center">
                                                            <h6 class="mb-0 text-sm">MAJALIWA WILFRED</h6>
                                                            {{-- <p class="text-xs text-secondary mb-0">
                                                                michael@creative-tim.com</p> --}}
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>
                                                    <p class="text-center text-xs font-weight-bold mb-0">wilfredmajaliwa@gmail.com</p>
                                                    <p class="text-xs text-secondary mb-0"></p>
                                                </td>
                                                <td>
                                                    <p class="text-center text-xs font-weight-bold mb-0">17/X/18558/EVE</p>
                                                    <p class="text-xs text-secondary mb-0"></p>
                                                </td>
                                                <td class="align-middle text-center text-sm">
                                                    <span class="badge badge-sm bg-gradient-success">Online</span>
                                                </td>
                                                <td class="align-middle text-center">
                                                    <span
                                                        class="text-secondary text-xs font-weight-bold">1700714382</span>
                                                </td>
                                                {{-- <td class="align-middle">
                                                    <a href="javascript:;"
                                                        class="text-secondary font-weight-bold text-xs"
                                                        data-toggle="tooltip" data-original-title="Edit user">
                                                        Edit
                                                    </a>
                                                </td> --}}
                                            </tr>
                                            <tr>
                                                <td>
                                                    <div class="d-flex px-2 py-1">
                                                        {{-- <div>
                                                            <img src="{{ asset('assets') }}/img/team-2.jpg"
                                                                class="avatar avatar-sm me-3 border-radius-lg"
                                                                alt="user5">
                                                        </div> --}}
                                                        <div class="d-flex flex-column justify-content-center">
                                                            <h6 class="mb-0 text-sm">AMADILE MAJID</h6>
                                                            {{-- <p class="text-xs text-secondary mb-0">
                                                                richard@creative-tim.com</p> --}}
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>
                                                    <p class="text-center text-xs font-weight-bold mb-0">amadilemajid10@gmail.com</p>
                                                    <p class="text-xs text-secondary mb-0"></p>
                                                </td>
                                                <td>
                                                    <p class="text-center text-xs font-weight-bold mb-0">20/U/23418</p>
                                                    <p class="text-xs text-secondary mb-0"></p>
                                                </td>
                                                <td class="align-middle text-center text-sm">
                                                    <span class="badge badge-sm bg-gradient-success">Online</span>
                                                </td>
                                                <td class="align-middle text-center">
                                                    <span
                                                        class="text-secondary text-xs font-weight-bold">2000723418</span>
                                                </td>
                                                {{-- <td class="align-middle">
                                                    <a href="javascript:;"
                                                        class="text-secondary font-weight-bold text-xs"
                                                        data-toggle="tooltip" data-original-title="Edit user">
                                                        Edit
                                                    </a>
                                                </td> --}}
                                            </tr>
                                            
                                        </tbody>
                                    </table>
                                                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                    
                
                <x-footers.auth></x-footers.auth>
            </div>
    </main>
    <x-plugins></x-plugins>

</x-layout>
