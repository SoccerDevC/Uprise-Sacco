<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Carbon\Carbon;
use App\Models\Deposits;
use App\Models\Registered_Loans;
use App\Models\Loan_Payments;

class Members extends Model
{
    // Assuming you have a 'deposits' table related by member_id
    public function deposits()
    {
        return $this->hasMany(Deposits::class, 'member_id', 'member_id');
        
    }

    public function registeredloans()
    {
        return $this->hasMany(Registered_Loans::class, 'member_id', 'member_id');
    }

    public function loanpayments()
    {
        return $this->hasMany(Loan_Payments::class, 'member_id', 'member_id');
    }


    protected $primaryKey = 'member_id';
    public $incrementing = false;
}
