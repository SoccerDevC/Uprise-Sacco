<?php
namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Failed_Login;

class DashboardController extends Controller
{
    public function index()
    {
        $records = $this->failed();

        return view('dashboard.index', ['records' => $records]);
    }

    public function failed()
    {
        $records = Failed_Login::all();
        return $records;
    }
}
